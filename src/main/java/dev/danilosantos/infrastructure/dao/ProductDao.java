package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.database.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductDao implements InterfaceProductDao {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final Connection connection = factory.getConnection();

    @Override
    public void insert(Product product) {

        try {
            String sql = "INSERT INTO produtos (hash, descricao, nome, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, product.getHash(), java.sql.Types.OTHER);
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
    }

    public void updateByHash(UUID hash, Product product) {
        try {
            String sql =
                    "UPDATE produtos " +
                    "SET descricao = ?, preco = ?, quantidade = ?, estoque_min = ?, dtupdate = ?" +
                    "WHERE hash = ?;";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(6, hash, java.sql.Types.OTHER);
            statement.setString(1, product.getDescription());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getQuantity());
            statement.setDouble(4, product.getMinStorage());
            statement.setTimestamp(5, getTimeStampOrNull(product.getDtUpdate()));

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            statement.close();
            resultSet.close();
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM produtos";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getLong("id"),
                        UUID.fromString(rs.getString("hash")),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getString("ean13"),
                        rs.getDouble("preco"),
                        rs.getDouble("quantidade"),
                        rs.getDouble("estoque_min"),
                        rs.getTimestamp("dtcreate"),
                        rs.getTimestamp("dtupdate"),
                        rs.getBoolean("lativo")
                );
                list.add(product);
            }
            statement.close();
            rs.close();
            return list;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Product findByHash(UUID param) {
        String sql = "SELECT * FROM produtos WHERE hash = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, param);
            ResultSet rs = statement.executeQuery();

            return getProduct(statement, rs);
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean deleteByHash(UUID hash) {
        String sql = "DELETE FROM produtos WHERE hash = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, hash);
            if(statement.executeUpdate() != 0) {
                return true;
            }
            statement.close();
            return false;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Product findByName(String param) {
        String sql = "SELECT * FROM produtos WHERE LOWER(nome) = LOWER(?)";
        return getProductFromDb(param, sql);
    }

    @Override
    public Product findByEan13(String param) {
        String sql = "SELECT * FROM produtos WHERE LOWER(ean13) = LOWER(?)";
        return getProductFromDb(param, sql);
    }

    @Override
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

    public void changeToActiveByHash(UUID hash) {
        try {
            String sql =
                    "UPDATE produtos " +
                            "SET lativo = ?" +
                            "WHERE hash = ?;";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(2, hash, java.sql.Types.OTHER);
            statement.setBoolean(1, true);

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            statement.close();
            resultSet.close();
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

            return getProduct(statement, rs);
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Product getProduct(PreparedStatement statement, ResultSet rs) throws SQLException {
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

    private Timestamp getTimeStampOrNull(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Timestamp(date.getTime());
        }
    }
}
