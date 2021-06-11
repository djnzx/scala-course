package a_interview;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SQLApp {

  interface BiConsumer<A, B> {
    void run(A a, B b) throws SQLException;
  }

  public ResultSet performQuery1(PreparedStatement stmt, String p1, Integer p2, Double p3) throws SQLException {
    stmt.setString(1, p1);
    stmt.setInt(2, p2);
    stmt.setDouble(3, p3);
    return stmt.executeQuery();
  }

  public ResultSet performQuery2(PreparedStatement stmt, String p1, Integer p2, Integer p3, Double p4) throws SQLException {
    stmt.setString(1, p1);
    stmt.setInt(2, p2);
    stmt.setInt(3, p3);
    stmt.setDouble(4, p4);
    return stmt.executeQuery();
  }

  public ResultSet performQuery(PreparedStatement stmt, List<BiConsumer<PreparedStatement, Integer>> setters) {
    throw new IllegalArgumentException();
  }

  public ResultSet perform(PreparedStatement stmt, String p1, Integer p2, Integer p3, Double p4) {
    return performQuery(stmt, Arrays.asList(
        (st, n) -> st.setString(n, p1),
        (st, n) -> st.setInt(n, p2),
        (st, n) -> st.setInt(n, p3),
        (st, n) -> st.setDouble(n, p4)
        )
    );
  }

}
