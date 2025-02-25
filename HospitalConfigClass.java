package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class HospitalConfigClass {
	
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "firstbit";
	
	public static void main(String[] args)
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		try{
			Connection connection = DriverManager.getConnection(url,username,password);
			Patient patient = new Patient(connection,sc);
			Doctor dr = new Doctor(connection);
			
			while(true)
			{
				System.out.println("********--HOSPITAL MANAGEMENT SYSTEM--********");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patient");
				System.out.println("3. View Doctor");
				System.out.println("4. Fix Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter your choice : ");
				int choice=sc.nextInt();
				
				switch(choice) {
				case 1:
					//add patient
					patient.addPatient();
					System.out.println();
					break;
				case 2:
					//view patient
					patient.viewpatient();
					System.out.println();
					break;
				case 3:
					//view doctor
					dr.viewDoctors();
					System.out.println();
					break;
				case 4:
					//fix appointment
					fixAppointment(patient,dr,connection,sc);
					System.out.println();
					break;
				case 5:
					return;
					default:
						System.out.println("Enter valid choice...");
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void fixAppointment(Patient patient, Doctor dr, Connection con,Scanner sc )
	{
		System.out.println("Enter patient's id : ");
		int patientId = sc.nextInt();
		System.out.println("Enter doctor's id : ");
		int drId =  sc.nextInt();
		System.out.println("Enter Appointment date in YYYY-MM-DD");
		String date=sc.next();
		if(patient.getPatientById(patientId) && dr.getDoctorById(drId))
		{
			if(checkDrAvailibility(drId,date,con))
			{
				String appointmentQuery = "insert into appointment(dr_id,patient_id,appointment_date) values(?,?,?)";
				try {
					PreparedStatement prepdstmt = con.prepareStatement(appointmentQuery);
					prepdstmt.setInt(1, drId);
					prepdstmt.setInt(2, patientId);
					prepdstmt.setString(3, date);
					int rowscount = prepdstmt.executeUpdate();
					
					if(rowscount>0)
					{
						System.out.println("Appointment booked..");
					}
					else
					{
						System.out.println("failed to book appointment..");
					}
					
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				
			}
			else
			{
				System.out.println("Doctor not available on this date");
			}
			
		}
		else
		{
			System.out.println("Either doctor or patient not available...");
		}
	}
	public static boolean checkDrAvailibility(int docId, String AppointmentDate, Connection con) {
		
		String Query = "select count(*) from appointment where dr_id =? AND appointment_date =?";
		try {
			PreparedStatement prepdstmt = con.prepareStatement(Query);
			prepdstmt.setInt(1, docId);
			prepdstmt.setString(2,AppointmentDate);
			ResultSet resultset = prepdstmt.executeQuery();
			if(resultset.next())
			{
				int count = resultset.getInt(1);
				if(count==0)
					return true;
				else
					return false;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
		
	}

}
