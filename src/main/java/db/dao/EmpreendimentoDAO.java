package db.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmpreendimentoDAO {
    private final MongoCollection<Document> collection;

    public EmpreendimentoDAO(MongoDatabase database) {
        this.collection = database.getCollection("empreendimentos");
    }

    public List<Empreendimento> buscarTodosEmpreendimentos() {
        List<Empreendimento> empreendimentos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Empreendimento empreendimento = new Empreendimento();
            empreendimento.setMatricula(doc.getString("matricula"));
            empreendimento.setTipoEmpreendimento(doc.getString("tipoEmpreendimento"));
            empreendimento.setComissaoMaxima(doc.getDouble("comissaoMaxima").floatValue());
            empreendimento.setDescricao(doc.getString("descricao"));
            empreendimento.setValor(doc.getDouble("valor").floatValue());
            empreendimento.setPontoVenda(doc.getString("pontoVenda"));
            empreendimento.setMargemNegociacao(doc.getDouble("margemNegociacao").floatValue());
            empreendimento.setProjetoUrbanistico(doc.getString("projetoUrbanistico"));

            // Criando o objeto Empresa
            Document empresaDoc = doc.get("empresaResponsavel", Document.class);
            if (empresaDoc != null) {
                Empresa empresa = new Empresa();
                empresa.setRazaoSocial(empresaDoc.getString("razaoSocial"));
                empresa.setCnpj(empresaDoc.getString("cnpj"));
                empresa.setCreci(empresaDoc.getString("creci"));
                empresa.setComissao(empresaDoc.getDouble("comissao").floatValue());

                // Criando o objeto Responsavel
                Document responsavelDoc = empresaDoc.get("responsavel", Document.class);
                if (responsavelDoc != null) {
                    Responsavel responsavel = new Responsavel();

                    // Criando o objeto Contato do Responsavel
                    Document contatoDoc = responsavelDoc.get("contato", Document.class);
                    if (contatoDoc != null) {
                        Contato contato = new Contato();
                        contato.setNome(contatoDoc.getString("nome"));
                        contato.setTelefone(contatoDoc.getString("telefone"));
                        contato.setEmail(contatoDoc.getString("email"));
                        contato.setEndereco(contatoDoc.getString("endereco"));
                        responsavel.setContato(contato);
                    }

                    responsavel.setCpf(responsavelDoc.getString("cpf"));
                    responsavel.setContratoSocial(responsavelDoc.getString("contratoSocial"));
                    empresa.setResponsavel(responsavel);
                }

                empreendimento.setEmpresaResponsavel(empresa);
            }

            // Criando o objeto RegistroCompra
            Document registroDoc = doc.get("registroCompra", Document.class);
            if (registroDoc != null) {
                RegistroCompra registroCompra = new RegistroCompra();

                // Criando o objeto Cliente
                Document clienteDoc = registroDoc.get("cliente", Document.class);
                if (clienteDoc != null) {
                    Cliente cliente = new Cliente();
                    Contato contato = new Contato();
                    contato.setNome(clienteDoc.getString("nome"));
                    contato.setTelefone(clienteDoc.getString("telefone"));
                    contato.setEmail(clienteDoc.getString("email"));
                    contato.setEndereco(clienteDoc.getString("endereco"));
                    cliente.setContato(contato);
                    cliente.setTipoCliente(clienteDoc.getString("tipoCliente"));
                    registroCompra.setCliente(cliente);
                }

                registroCompra.setCpf(registroDoc.getString("cpf"));
                registroCompra.setRg(registroDoc.getString("rg"));
                registroCompra.setEstadoCivil(registroDoc.getString("estadoCivil"));
                registroCompra.setRegimeEstadoCivil(registroDoc.getString("regimeEstadoCivil"));
                registroCompra.setCondicoesPagamento(registroDoc.getString("condicoesPagamento"));

                empreendimento.setRegistroCompra(registroCompra);
            }

            empreendimentos.add(empreendimento);
        }

        return empreendimentos;
    }

    public void salvarEmpreendimento(Empreendimento empreendimento) {
        // Documento da empresa responsável
        Document empresaDoc = null;
        if (empreendimento.getEmpresaResponsavel() != null) {
            Empresa empresa = empreendimento.getEmpresaResponsavel();

            // Documento do Responsável
            Document responsavelDoc = null;
            if (empresa.getResponsavel() != null) {
                Responsavel responsavel = empresa.getResponsavel();
                Document contatoDoc = null;

                // Documento do Contato do Responsável
                if (responsavel.getContato() != null) {
                    Contato contato = responsavel.getContato();
                    contatoDoc = new Document("nome", contato.getNome())
                            .append("telefone", contato.getTelefone())
                            .append("email", contato.getEmail())
                            .append("endereco", contato.getEndereco());
                }

                responsavelDoc = new Document("contato", contatoDoc)
                        .append("cpf", responsavel.getCpf())
                        .append("contratoSocial", responsavel.getContratoSocial());
            }

            empresaDoc = new Document("razaoSocial", empresa.getRazaoSocial())
                    .append("cnpj", empresa.getCnpj())
                    .append("creci", empresa.getCreci())
                    .append("comissao", empresa.getComissao())
                    .append("responsavel", responsavelDoc);
        }

        // Documento do registro de compra
        Document registroDoc = null;
        if (empreendimento.getRegistroCompra() != null) {
            RegistroCompra registroCompra = empreendimento.getRegistroCompra();

            // Documento do cliente
            Document clienteDoc = null;
            if (registroCompra.getCliente() != null) {
                Cliente cliente = registroCompra.getCliente();
                Contato contato = cliente.getContato();
                clienteDoc = new Document("nome", contato.getNome())
                        .append("telefone", contato.getTelefone())
                        .append("email", contato.getEmail())
                        .append("endereco", contato.getEndereco())
                        .append("tipoCliente", cliente.getTipoCliente());
            }

            registroDoc = new Document("cliente", clienteDoc)
                    .append("cpf", registroCompra.getCpf())
                    .append("rg", registroCompra.getRg())
                    .append("estadoCivil", registroCompra.getEstadoCivil())
                    .append("regimeEstadoCivil", registroCompra.getRegimeEstadoCivil())
                    .append("condicoesPagamento", registroCompra.getCondicoesPagamento());
        }

        // Criando o documento do empreendimento
        Document empreendimentoDoc = new Document("matricula", empreendimento.getMatricula())
                .append("tipoEmpreendimento", empreendimento.getTipoEmpreendimento())
                .append("empresaResponsavel", empresaDoc)
                .append("comissaoMaxima", empreendimento.getComissaoMaxima())
                .append("descricao", empreendimento.getDescricao())
                .append("valor", empreendimento.getValor())
                .append("pontoVenda", empreendimento.getPontoVenda())
                .append("margemNegociacao", empreendimento.getMargemNegociacao())
                .append("projetoUrbanistico", empreendimento.getProjetoUrbanistico())
                .append("registroCompra", registroDoc);

        collection.insertOne(empreendimentoDoc); // Inserindo o empreendimento no MongoDB
    }
}