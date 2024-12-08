package db.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import db.MongoDBConnection;
import models.Cliente;
import models.Contato;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final MongoCollection<Document> collection;

    public ClienteDAO(MongoDatabase database) {
        this.collection = database.getCollection("clientes");
    }

    public List<Cliente> buscarTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();

        for (Document doc : collection.find()) {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(doc.getObjectId("_id").toString());

            // Criando o objeto Contato a partir dos campos armazenados no MongoDB
            Contato contato = new Contato();
            contato.setNome(doc.getString("nome"));
            contato.setTelefone(doc.getString("telefone"));
            contato.setEmail(doc.getString("email"));
            contato.setEndereco(doc.getString("endereco"));

            cliente.setContato(contato);
            cliente.setTipoCliente(doc.getString("status"));

            clientes.add(cliente);
        }

        return clientes;
    }

    public void salvarCliente(Cliente cliente) {
        // Extraindo os dados de contato e armazenando-os diretamente no documento de cliente
        Document contatoDoc = new Document("nome", cliente.getContato().getNome())
                .append("telefone", cliente.getContato().getTelefone())
                .append("email", cliente.getContato().getEmail())
                .append("endereco", cliente.getContato().getEndereco());

        // Criando o documento para o cliente
        Document clienteDoc = new Document("nome", cliente.getContato().getNome()) // "nome" aqui, não "idCliente"
                .append("telefone", cliente.getContato().getTelefone())
                .append("email", cliente.getContato().getEmail())
                .append("endereco", cliente.getContato().getEndereco())
                .append("status", cliente.getTipoCliente()) // Armazenando o status como campo
                .append("observacoes", cliente.getObservacoes());

        collection.insertOne(clienteDoc); // Inserindo o cliente no MongoDB
    }
}