<?php 
	
		
		//Getting values
		$name = $_POST['name'];
		$password = $_POST['pass'];
		
		$sql = "select * from users_info where name='$name' and password='$password' ";
		
		require_once('dbConnect.php');
		$result=mysqli_query($con,$sql);
		
		if($result->num_rows > 0){
			echo 'yes';
		}else{
			echo'no';
		}
		mysqli_close($con);
	 