package common;

import java.sql.SQLException;

public interface Task
{
    public void execute(Controller controller) throws SQLException;
}
