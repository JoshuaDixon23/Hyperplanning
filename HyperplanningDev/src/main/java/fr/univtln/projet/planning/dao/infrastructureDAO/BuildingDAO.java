
package fr.univtln.projet.planning.dao.infrastructureDAO;
import fr.univtln.projet.planning.dao.GenericDAO;
import fr.univtln.projet.planning.entity.infrastructure.Building;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;





public class BuildingDAO implements GenericDAO<Building, Integer> {

    private Connection connection;

    public BuildingDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Building building) throws SQLException {
        String sql = "INSERT INTO Building (localisation, opening_time, closing_time, ufr_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, building.getLocalisation());
            pstmt.setInt(2, building.getOpeningHours());
            pstmt.setString(3, building.getMap());
            pstmt.setInt(4, building.getUfr());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Building read(Integer id) throws SQLException {
        String sql = "SELECT * FROM Building WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Building.BuildingFactory(
                            rs.getString("name"),
                            rs.getString("localisation"),
                            rs.getString("opening_hours")

                    );
                }
            }
        }
        return null;
    }

    @Override
    public void update(Building building) throws SQLException {
        String sql = "UPDATE Building SET localisation = ?, opening_time = ?, closing_time = ?, ufr_id = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, building.localisation());
            pstmt.setString(2, building.GetOpening());
            pstmt.setString(3, building.closingTime());
            pstmt.setInt(4, building.ufrId());
            pstmt.setInt(5, building.id());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Building WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Building> findAll() throws SQLException {
        List<Building> buildings = new ArrayList<>();

        String sql = "SELECT * FROM Building";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                buildings.add(new Building(
                        rs.getInt("id"),
                        rs.getString("localisation"),
                        rs.getString("opening_time"),
                        rs.getString("closing_time"),
                        rs.getInt("ufr_id")
                ));
            }
        }

        return buildings;
    }
}





