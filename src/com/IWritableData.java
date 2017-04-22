package com;

import java.sql.Connection;
import java.sql.SQLException;

public interface IWritableData {
	public void writeToDB(Connection con) throws SQLException;
}
