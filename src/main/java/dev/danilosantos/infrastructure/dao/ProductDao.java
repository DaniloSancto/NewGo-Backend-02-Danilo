package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.database.ConnectionFactory;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class ProductDao {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Connection connection = factory.getConnection();

    public Product insert(Product product) {

        try {
            String sql = "INSERT INTO produtos (hash, descricao, nome, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, product.getHash(),java.sql.Types.OTHER);
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getName());
            preparedStatement.setString(4, product.getEan13());
            preparedStatement.setDouble(5, product.getPrice());
            preparedStatement.setDouble(6, product.getQuantity());
            preparedStatement.setDouble(7, product.getMinStorage());
            preparedStatement.setTimestamp(8, getTimeStampOrNull(product.getDtCreate()));
            preparedStatement.setTimestamp(9, getTimeStampOrNull(product.getDtUpdate()));
            preparedStatement.setBoolean(10, product.getActive());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            Long generatedId = resultSet.getLong("id");
            product.setId(generatedId);

            preparedStatement.close();
            resultSet.close();

        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return product;
    }

    public Product findByName(String param) {
        String sql = "SELECT * FROM produtos WHERE LOWER(nome) = LOWER(?)";
        return getProductFromDb(param, sql);
    }

    public Product findByEan13(String param) {
        String sql = "SELECT * FROM produtos WHERE LOWER(ean13) = LOWER(?)";
        return getProductFromDb(param, sql);
    }

    public UUID findHash(UUID param) {
        String sql = "SELECT hash FROM produtos WHERE hash = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, param);
            ResultSet rs = statement.executeQuery();
            UUID hash = null;
            if (rs.next()) {
                hash = UUID.fromString(rs.getString("hash"));
            }
            statement.close();
            rs.close();
            return hash;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Product getProductFromDb(String param, String sql) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, param);
            ResultSet rs = statement.executeQuery();

            Product product = null;
            if (rs.next()) {
                product = new Product(
                        rs.getLong("id"),
                        UUID.fromString(rs.getString("hash")),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getString("ean13"),
                        rs.getDouble("preco"),
                        rs.getDouble("quantidade"),
                        rs.getDouble("estoque_min"),
                        rs.getTimestamp("dtcreate"),
                        rs.getDate("dtupdate"),
                        rs.getBoolean("lativo")
                );
            }
            statement.close();
            rs.close();
            return product;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Timestamp getTimeStampOrNull(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Timestamp(date.getTime());
        }
    }
}
