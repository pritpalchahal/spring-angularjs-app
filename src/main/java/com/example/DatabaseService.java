package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class DatabaseService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Sets up database tables. This just runs the setup.sql script, which sets up tables if they don't already exist.
	 * @return true if tables setup, false if an error occurred and the tables were not setup. 
	 * @throws IOException
	 */
	public boolean setupTables() throws IOException, NoSuchAlgorithmException
	{
		InputStream sqlInput = PingController.class.getResourceAsStream("/setup.sql");
		
		// Originally from: http://stackoverflow.com/questions/1497569/how-to-execute-sql-script-file-using-jdbc
		Scanner s = new Scanner(sqlInput);
		s.useDelimiter("(;(\r)?\n)|(--\n)");
		try
		{
			while (s.hasNext())
			{
				String line = s.next();
				if (line.startsWith("/*!") && line.endsWith("*/"))
				{
					int i = line.indexOf(' ');
					line = line.substring(i + 1, line.length() - " */".length());
				}

				if (line.trim().length() > 0)
				{
					jdbcTemplate.execute(line);
				}
			}
		}
		catch(DataAccessException exception)
		{
			exception.printStackTrace();
			s.close();
			sqlInput.close();
			return false;
		}
		s.close();
		sqlInput.close();
		
			
		// Setup default mappings
		
		return true;
	}
}
