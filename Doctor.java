package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctor {

	private Connection connection;

	public Doctor(Connection connection) {
		super();
		this.connection = connection;

	}

	public void viewDoctors() {
		String query = "select * from doctor";
		try {
			PreparedStatement preparedstatement = connection.prepareStatement(query);
			ResultSet resultset = preparedstatement.executeQuery();
			System.out.println("Available Doctors : ");
			System.out.println("+------------+------------------+--------------------+");
			System.out.println("| Doctor Id  | Dr. Name         | Dr. Specialization |");
			System.out.println("+------------+------------------+--------------------+");
			while (resultset.next()) {

				int id = resultset.getInt("dr_id"); // SQL field names on RHS, local java variables on LHS
				String name = resultset.getString("dr_name");
				String specialization = resultset.getString("dr_specialization");
				System.out.printf("| %-10s | %-16s | %-18s |\n",id,name,specialization);
				System.out.println("+------------+------------------+--------------------+");
			}
		} catch (Exception e) {

		}
	}

	public boolean getDoctorById(int id) {
		String query = "select * from doctor where dr_id = ?";

		try {

			PreparedStatement prepstmt = connection.prepareStatement(query);
			prepstmt.setInt(1, id);
			ResultSet resultset = prepstmt.executeQuery();
			if (resultset.next()) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
