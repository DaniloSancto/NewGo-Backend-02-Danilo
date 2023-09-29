package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.entities.Product;
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
            preparedStatement.setString(2, product.getDescricao());
            preparedStatement.setString(3, product.getNome());
            preparedStatement.setString(4, product.getEan13());
            preparedStatement.setDouble(5, product.getPreco());
            preparedStatement.setDouble(6, product.getQuantidade());
            preparedStatement.setDouble(7, product.getEstoqueMin());
            preparedStatement.setTimestamp(8, getTimeStampOrNull(product.getDtCreate()));
            preparedStatement.setTimestamp(9, getTimeStampOrNull(product.getDtUpdate()));
            preparedStatement.setBoolean(10, product.getLAtivo());

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
            statement.setString(1, product.getDescricao());
            statement.setDouble(2, product.getPreco());
            statement.setDouble(3, product.getQuantidade());
            statement.setDouble(4, product.getEstoqueMin());
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
        return getListOfProductFromDb("");
    }

    public Product findByHash(UUID param) {
        return getOneProductFromDb(param, "WHERE hash = ?");
    }

    @Override
    public void deleteByHash(UUID hash) {
        String sql = "DELETE FROM produtos WHERE hash = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, hash);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Product findByName(String param) {
        return getOneProductFromDb(param, "WHERE LOWER(nome) = LOWER(?)");
    }

    @Override
    public Product findByEan13(String param) {
        return getOneProductFromDb(param, "WHERE LOWER(ean13) = LOWER(?)");
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

    public void changeLAtivoToTrue(UUID hash) {
        updateLAtivoOnDb(hash, true);
    }

    public void changeLAtivoToFalse(UUID hash) {
        updateLAtivoOnDb(hash, false);
    }

    public Product findActiveProduct(UUID param) {
        return getOneProductFromDb(param, "WHERE hash = ? AND lativo = true");
    }

    @Override
    public List<Product> findAllActiveProducts() {
        return getListOfProductFromDb("WHERE lativo = true");
    }

    @Override
    public List<Product> findAllInactiveProducts() {
        return getListOfProductFromDb("WHERE lativo = false");
    }

    @Override
    public List<Product> findAllQuantityLowerStorageProducts() {
        return getListOfProductFromDb("WHERE quantidade < estoque_min AND lativo = true");
    }

    private Product getOneProductFromDb(Object param, String condition) {
        PreparedStatement statement;
        Product product = null;
        String sql = "SELECT * FROM produtos " + condition;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, param);
            ResultSet rs = statement.executeQuery();

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
                    rs.getTimestamp("dtupdate"),
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

    private List<Product> getListOfProductFromDb(String condition) {
        String sql = "SELECT * FROM produtos " + condition;
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

    public void updateProductPrice (UUID hash, Double newPrice) {
        try {
            String sql =
                    "UPDATE produtos " +
                    "SET preco = ?, dtupdate = ?" +
                    "WHERE hash = ?;";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(3, hash, java.sql.Types.OTHER);
            statement.setDouble(1, newPrice);
            statement.setTimestamp(2, getTimeStampOrNull(new Date()));

            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateLAtivoOnDb(UUID hash, boolean condition) {
        try {
            String sql =
                    "UPDATE produtos " +
                    "SET lativo = ?" +
                    "WHERE hash = ?;";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(2, hash, java.sql.Types.OTHER);
            statement.setBoolean(1, condition);

            statement.executeUpdate();
            statement.close();
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
