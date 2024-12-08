package db.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import db.MongoDBConnection;
import models.Atendimento;
import models.Cliente;
import models.Empresa;
import models.Funcionario;
import org.bson.Document;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AtendimentoDAO {
    private final MongoCollection<Document> collection;

    public AtendimentoDAO(MongoDatabase database) {
        this.collection = database.getCollection("atendimentos");
    }

    private Cliente getClienteById(String clienteId) {
        // Buscando o cliente com o ID fornecido (exemplo com MongoDB)
        MongoCollection<Document> clienteCollection = MongoDBConnection.getDatabase().getCollection("clientes");
        Document clienteDoc = clienteCollection.find(Filters.eq("_id", clienteId)).first();

        Cliente cliente = new Cliente();
        if (clienteDoc != null) {
            cliente.setIdCliente(clienteDoc.getString("_id"));
            // Preencher outros campos conforme necessário
        }
        return cliente;
    }

    private Empresa getEmpresaById(String empresaId) {
        MongoCollection<Document> empresaCollection = MongoDBConnection.getDatabase().getCollection("empresas");
        Document empresaDoc = empresaCollection.find(Filters.eq("_id", empresaId)).first();

        Empresa empresa = new Empresa();
        if (empresaDoc != null) {
            empresa.setRazaoSocial(empresaDoc.getString("razaoSocial"));
            // Preencher outros campos conforme necessário
        }
        return empresa;
    }

    private Funcionario getFuncionarioById(String funcionarioId) {
        MongoCollection<Document> funcionarioCollection = MongoDBConnection.getDatabase().getCollection("funcionarios");
        Document funcionarioDoc = funcionarioCollection.find(Filters.eq("_id", funcionarioId)).first();

        Funcionario funcionario = new Funcionario();
        if (funcionarioDoc != null) {
            funcionario.setNome(funcionarioDoc.getString("nome"));
            // Preencher outros campos conforme necessário
        }
        return funcionario;
    }

    public List<Atendimento> buscarTodosAtendimentos() {
        List<Atendimento> atendimentos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Atendimento atendimento = new Atendimento();

            // Cliente
            Document clienteDoc = doc.get("cliente", Document.class);
            if (clienteDoc != null) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(clienteDoc.getString("idCliente"));
                cliente.setContato(null); // Substitua para carregar o contato, se necessário
                cliente.setTipoCliente(clienteDoc.getString("tipoCliente"));
                cliente.setObservacoes(clienteDoc.getString("observacoes"));
                atendimento.setCliente(cliente);
            }

            // Empresa
            Document empresaDoc = doc.get("agenteEmpresa", Document.class);
            if (empresaDoc != null) {
                Empresa empresa = new Empresa();
                empresa.setRazaoSocial(empresaDoc.getString("razaoSocial"));
                empresa.setCnpj(empresaDoc.getString("cnpj"));
                atendimento.setAgenteEmpresa(empresa);
            }

            // Funcionário
            Document funcionarioDoc = doc.get("agenteFuncionario", Document.class);
            if (funcionarioDoc != null) {
                Funcionario funcionario = new Funcionario();
                funcionario.setNome(funcionarioDoc.getString("nome"));
                funcionario.setCpf(funcionarioDoc.getString("cpf"));
                atendimento.setAgenteFuncionario(funcionario);
            }

            // Outros campos
            atendimento.setDataVisita(doc.getDate("dataVisita"));
            atendimento.setHorarioVisita(new Time(doc.getLong("horarioVisita")));
            atendimento.setLocarEncontro(doc.getString("locarEncontro"));
            atendimento.setSituacao(doc.getString("situacao"));
            atendimento.setObservacoesAtendimento(doc.getString("observacoesAtendimento"));

            atendimentos.add(atendimento);
        }

        return atendimentos;
    }

    public List<Atendimento> buscarAtendimentosPorStatus(String status) {
        List<Atendimento> atendimentos = new ArrayList<>();
        Document query = new Document("situacao", status);
        for (Document doc : collection.find(query)) {
            atendimentos.add(convertDocumentToAtendimento(doc));
        }
        return atendimentos;
    }

    private Atendimento convertDocumentToAtendimento(Document doc) {
        Atendimento atendimento = new Atendimento();

        // Recuperando o Cliente associado
        String clienteId = doc.getString("clienteId");  // Supondo que o cliente esteja identificado pelo ID
        Cliente cliente = getClienteById(clienteId);  // Método para buscar Cliente no banco
        atendimento.setCliente(cliente);

        // Recuperando a Empresa associada
        String empresaId = doc.getString("empresaId");  // Supondo que a empresa esteja identificada pelo ID
        Empresa empresa = getEmpresaById(empresaId);  // Método para buscar Empresa no banco
        atendimento.setAgenteEmpresa(empresa);

        // Recuperando o Funcionário associado
        String funcionarioId = doc.getString("funcionarioId");  // Supondo que o funcionário esteja identificado pelo ID
        Funcionario funcionario = getFuncionarioById(funcionarioId);  // Método para buscar Funcionario no banco
        atendimento.setAgenteFuncionario(funcionario);

        // Definindo a data e horário da visita
        atendimento.setDataVisita(doc.getDate("dataVisita"));  // A data de visita do documento
        atendimento.setHorarioVisita(new java.sql.Time(doc.getDate("horarioVisita").getTime())); // Convertendo para SQL Time

        // Definindo o local de encontro
        atendimento.setLocarEncontro(doc.getString("localEncontro"));

        // Definindo a situação
        atendimento.setSituacao(doc.getString("situacao"));

        // Definindo as observações do atendimento
        atendimento.setObservacoesAtendimento(doc.getString("observacoesAtendimento"));

        return atendimento;
    }

    public void salvarAtendimento(Atendimento atendimento) {
        Document atendimentoDoc = new Document();

        // Cliente
        Cliente cliente = atendimento.getCliente();
        if (cliente != null) {
            Document clienteDoc = new Document("idCliente", cliente.getIdCliente())
                    .append("tipoCliente", cliente.getTipoCliente())
                    .append("observacoes", cliente.getObservacoes());
            atendimentoDoc.append("cliente", clienteDoc);
        }

        // Empresa
        Empresa empresa = atendimento.getAgenteEmpresa();
        if (empresa != null) {
            Document empresaDoc = new Document("razaoSocial", empresa.getRazaoSocial())
                    .append("cnpj", empresa.getCnpj());
            atendimentoDoc.append("agenteEmpresa", empresaDoc);
        }

        // Funcionário
        Funcionario funcionario = atendimento.getAgenteFuncionario();
        if (funcionario != null) {
            Document funcionarioDoc = new Document("nome", funcionario.getContato().getNome())
                    .append("cpf", funcionario.getCpf());
            atendimentoDoc.append("agenteFuncionario", funcionarioDoc);
        }

        // Outros campos
        atendimentoDoc.append("dataVisita", atendimento.getDataVisita())
                .append("horarioVisita", atendimento.getHorarioVisita().getTime())
                .append("locarEncontro", atendimento.getLocarEncontro())
                .append("situacao", atendimento.getSituacao())
                .append("observacoesAtendimento", atendimento.getObservacoesAtendimento());

        collection.insertOne(atendimentoDoc);
    }

    public void atualizarAtendimento(String id, Atendimento atendimentoAtualizado) {
        Document filtro = new Document("_id", id);

        Document atendimentoDoc = new Document();

        // Cliente
        Cliente cliente = atendimentoAtualizado.getCliente();
        if (cliente != null) {
            Document clienteDoc = new Document("idCliente", cliente.getIdCliente())
                    .append("tipoCliente", cliente.getTipoCliente())
                    .append("observacoes", cliente.getObservacoes());
            atendimentoDoc.append("cliente", clienteDoc);
        }

        // Empresa
        Empresa empresa = atendimentoAtualizado.getAgenteEmpresa();
        if (empresa != null) {
            Document empresaDoc = new Document("razaoSocial", empresa.getRazaoSocial())
                    .append("cnpj", empresa.getCnpj());
            atendimentoDoc.append("agenteEmpresa", empresaDoc);
        }

        // Funcionário
        Funcionario funcionario = atendimentoAtualizado.getAgenteFuncionario();
        if (funcionario != null) {
            Document funcionarioDoc = new Document("nome", funcionario.getContato().getNome())
                    .append("cpf", funcionario.getCpf());
            atendimentoDoc.append("agenteFuncionario", funcionarioDoc);
        }

        // Outros campos
        atendimentoDoc.append("dataVisita", atendimentoAtualizado.getDataVisita())
                .append("horarioVisita", atendimentoAtualizado.getHorarioVisita().getTime())
                .append("locarEncontro", atendimentoAtualizado.getLocarEncontro())
                .append("situacao", atendimentoAtualizado.getSituacao())
                .append("observacoesAtendimento", atendimentoAtualizado.getObservacoesAtendimento());

        collection.replaceOne(filtro, atendimentoDoc);
    }

    public void deletarAtendimento(String id) {
        Document filtro = new Document("_id", id);
        collection.deleteOne(filtro);
    }
}