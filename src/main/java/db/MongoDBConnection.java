package db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://diegoodaniel:DiegoDaniel2208@imobsync.tnzt7.mongodb.net/?retryWrites=true&w=majority&appName=ImobSync";
    private static final String DATABASE_NAME = System.getenv("MONGO_DB") != null
            ? System.getenv("MONGO_DB")
            : "sistemaClientes";
    private static MongoClient client;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                client = MongoClients.create(CONNECTION_STRING);
                database = client.getDatabase(DATABASE_NAME);
                System.out.println("Conexão com MongoDB estabelecida!");
            } catch (Exception e) {
                System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
                throw new RuntimeException("Falha ao conectar ao banco de dados", e);
            }
        }
        return database;
    }

    public static void closeConnection() {
        if (client != null) {
            client.close();
            System.out.println("Conexão com MongoDB fechada.");
        }
    }

    public static void main(String[] args) {
        // Testar conexão
        MongoDatabase db = MongoDBConnection.getDatabase();
        System.out.println("Nome do banco de dados: " + db.getName());

        // Fechar conexão
        MongoDBConnection.closeConnection();
    }
}