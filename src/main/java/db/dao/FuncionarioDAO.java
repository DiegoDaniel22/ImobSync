package db.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import db.MongoDBConnection;
import models.Funcionario;
import models.Contato;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuncionarioDAO {
    private final MongoCollection<Document> collection;

    public FuncionarioDAO(MongoDatabase database) {
        this.collection = database.getCollection("funcionarios");
    }

    public List<Funcionario> buscarTodosFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de data padrão

        for (Document doc : collection.find()) {
            Funcionario funcionario = new Funcionario();

            // Criando o objeto Contato a partir dos campos armazenados no MongoDB
            Contato contato = new Contato();
            contato.setNome(doc.getString("nome"));
            contato.setTelefone(doc.getString("telefone"));
            contato.setEmail(doc.getString("email"));
            contato.setEndereco(doc.getString("endereco"));
            funcionario.setContato(contato);

            funcionario.setCpf(doc.getString("cpf"));
            funcionario.setFuncao(doc.getString("funcao"));
            funcionario.setComissao(doc.getDouble("comissao").floatValue());

            try {
                Date dataAdmissao = sdf.parse(doc.getString("dataAdmissao"));
                funcionario.setDataAdmissao(dataAdmissao);

                String dataDemissaoStr = doc.getString("dataDemissao");
                if (dataDemissaoStr != null) {
                    Date dataDemissao = sdf.parse(dataDemissaoStr);
                    funcionario.setDataDemissao(dataDemissao);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Tratar exceções de parsing de data
            }

            funcionarios.add(funcionario);
        }

        return funcionarios;
    }

    public void salvarFuncionario(Funcionario funcionario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de data padrão

        // Extraindo os dados de contato e armazenando-os diretamente no documento de funcionário
        Document contatoDoc = new Document("nome", funcionario.getContato().getNome())
                .append("telefone", funcionario.getContato().getTelefone())
                .append("email", funcionario.getContato().getEmail())
                .append("endereco", funcionario.getContato().getEndereco());

        // Criando o documento para o funcionário
        Document funcionarioDoc = new Document("nome", funcionario.getContato().getNome())
                .append("telefone", funcionario.getContato().getTelefone())
                .append("email", funcionario.getContato().getEmail())
                .append("endereco", funcionario.getContato().getEndereco())
                .append("cpf", funcionario.getCpf())
                .append("funcao", funcionario.getFuncao())
                .append("comissao", funcionario.getComissao())
                .append("dataAdmissao", sdf.format(funcionario.getDataAdmissao()))
                .append("dataDemissao", funcionario.getDataDemissao() != null ? sdf.format(funcionario.getDataDemissao()) : null);

        collection.insertOne(funcionarioDoc); // Inserindo o funcionário no MongoDB
    }
}