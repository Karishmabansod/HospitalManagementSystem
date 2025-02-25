package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
	
	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	}
	 
	public void addPatient() {
		System.out.println("Enter patient name : ");
		String name = scanner.next();
		System.out.println("Enter patient age : ");
		int age = scanner.nextInt();
		System.out.println("Enter patient gender : ");
		String gender = scanner.next();
		
		try {
			
			String Query = "insert into patient(patient_name,patient_age,patient_gender)values(?,?,?)";
			PreparedStatement preparedstatement = connection.prepareStatement(Query); 		
			preparedstatement.setString(1,name);
			preparedstatement.setInt(2,age);
			preparedstatement.setString(3,gender);
			int affectedrows = preparedstatement.executeUpdate();
			if(affectedrows>0)
			{
				System.out.println("patient added successfully!!");
			}
			else
			{
				System.out.println("failed to add patient!!");
			}
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void viewpatient()
	{
		String query="select * from patient";
		try {
			PreparedStatement preparedstatement = connection.prepareStatement(query);
			ResultSet resultset = preparedstatement.executeQuery();
			System.out.println("Patients List : ");
			System.out.println("+------------+------------------+----------+------------+");
			System.out.println("| Patient Id | Name             | Age      | Gender     |");
			System.out.println("+------------+------------------+----------+------------+");
			while (resultset.next()){
				
				int id = resultset.getInt("patient_id"); //SQL field names on RHS, local java variables on LHS
				String name = resultset.getString("patient_name");
				int age = resultset.getInt("patient_age");
				String gender = resultset.getString("patient_gender");
				System.out.printf("| %-10s | %-16s | %-8s | %-10s |\n",id,name,age,gender);
				System.out.println("+------------+------------------+----------+------------+");
			}
		}
		catch(Exception e){
			
		}
	}
	public boolean getPatientById(int id)
	{
		String query = "select * from patient where patient_id = ?";
		
		try {
			
			PreparedStatement prepstmt = connection.prepareStatement(query);
			prepstmt.setInt(1,id);
			ResultSet resultset = prepstmt.executeQuery();
			if(resultset.next())
			{
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	

}
