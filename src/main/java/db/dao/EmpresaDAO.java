package db.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import db.MongoDBConnection;
import models.Empresa;
import models.Responsavel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {
    private final MongoCollection<Document> collection;

    public EmpresaDAO(MongoDatabase database) {
        this.collection = database.getCollection("empresas");
    }

    public List<Empresa> buscarTodasEmpresas() {
        List<Empresa> empresas = new ArrayList<>();

        for (Document doc : collection.find()) {
            Empresa empresa = new Empresa();
            empresa.setRazaoSocial(doc.getString("razaoSocial"));
            empresa.setCnpj(doc.getString("cnpj"));
            empresa.setCreci(doc.getString("creci"));
            empresa.setComissao(doc.getDouble("comissao").floatValue());

            Document responsavelDoc = doc.get("responsavel", Document.class);
            if (responsavelDoc != null) {
                Responsavel responsavel = new Responsavel();
                responsavel.setCpf(responsavelDoc.getString("cpf"));
                responsavel.setContratoSocial(responsavelDoc.getString("contratoSocial"));
                empresa.setResponsavel(responsavel);
            }

            empresas.add(empresa);
        }

        return empresas;
    }

    public void salvarEmpresa(Empresa empresa) {
        Document empresaDoc = new Document("razaoSocial", empresa.getRazaoSocial())
                .append("cnpj", empresa.getCnpj())
                .append("creci", empresa.getCreci())
                .append("comissao", empresa.getComissao());

        Responsavel responsavel = empresa.getResponsavel();
        if (responsavel != null) {
            Document responsavelDoc = new Document("cpf", responsavel.getCpf())
                    .append("contratoSocial", responsavel.getContratoSocial());
            empresaDoc.append("responsavel", responsavelDoc);
        }

        collection.insertOne(empresaDoc);
    }

    public void atualizarEmpresa(String cnpj, Empresa empresaAtualizada) {
        Document filtro = new Document("cnpj", cnpj);

        Document empresaDoc = new Document("razaoSocial", empresaAtualizada.getRazaoSocial())
                .append("cnpj", empresaAtualizada.getCnpj())
                .append("creci", empresaAtualizada.getCreci())
                .append("comissao", empresaAtualizada.getComissao());

        Responsavel responsavel = empresaAtualizada.getResponsavel();
        if (responsavel != null) {
            Document responsavelDoc = new Document("cpf", responsavel.getCpf())
                    .append("contratoSocial", responsavel.getContratoSocial());
            empresaDoc.append("responsavel", responsavelDoc);
        }

        collection.replaceOne(filtro, empresaDoc);
    }

    public void deletarEmpresa(String cnpj) {
        Document filtro = new Document("cnpj", cnpj);
        collection.deleteOne(filtro);
    }
}