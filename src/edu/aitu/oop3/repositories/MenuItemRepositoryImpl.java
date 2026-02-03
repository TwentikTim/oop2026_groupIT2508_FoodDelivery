package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.MenuItem;
import java.util.List;


public class MenuItemRepositoryImpl extends BaseRepository<MenuItem, Integer> implements MenuItemRepository {

    public MenuItemRepositoryImpl(IDB db) {
        super(db);
    }

    private final RowMapper<MenuItem> menuItemMapper = rs -> new MenuItem(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            rs.getBoolean("available")
    );

    @Override
    public List<MenuItem> findAll() {
        String sql = "SELECT id, name, price, available FROM menu_items ORDER BY id";
        return query(sql, menuItemMapper);
    }

    @Override
    public MenuItem findById(Integer id) {
        String sql = "SELECT id, name, price, available FROM menu_items WHERE id = ?";
        List<MenuItem> items = query(sql, menuItemMapper, id);
        return items.isEmpty() ? null : items.get(0);
    }

    @Override
    public void updateAvailability(int id, boolean available) {
        String sql = "UPDATE menu_items SET available = ? WHERE id = ?";
        executeUpdate(sql, available, id);
    }
}
