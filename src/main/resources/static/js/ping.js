angular.module('ping', [])
  .controller('mainCtrl', function($scope, $http, $interval) {
	$scope.hosts = [];
	
	/**
	*	Flips host selection.
	*/
	$scope.select = function(host){
		host.isSelected = !host.isSelected;
		if(host.isSelected){
			$scope.out = "Selected: "+host.name;
		}
		else{
			$scope.out = "Deselected: "+host.name;
		}
	}
	
	/**
	*	Removes selected hosts from the system.
	*/
	$scope.delete = function(){
		var count = 0;
		for(var i=0;i<$scope.hosts.length;i++){
			if($scope.hosts[i].isSelected){
				var val = $scope.hosts[i].name;
				print("Deleted: "+val);
				count++;
				$scope.hosts.splice(i,1);
				i--;
			}
		}
		$scope.out = "Deleted: "+count+" hosts.";
	}
	
	/**
	*	Adds a new host to the system after validation from server.
	*/	
	$scope.addHost = function(){
		//change focus to input text box
		document.querySelector("#input_box").focus();
		
		try{
			//nothing supplied
			if(!$scope.host) throw "No input!";
			$scope.out = "";
			
			var host_name = $scope.host.toLowerCase();
			
			//check if name already exists
			angular.forEach($scope.hosts,function(host){
				if(host.name == host_name){
					throw host_name+" already exists!";
				}
			})
	
			//validate host name from server
			$http.post('/add-host',host_name)
				.success(function(data){
					if(!data) {
						console.log("Input invalid!");
						$scope.out = "Input invalid!";
						return;
					}
					$scope.host = "";//clear the input text box
					var obj = {"name":host_name,"time":null,"isSelected":false};
					$scope.hosts.push(obj);
					console.log("Add successful.");
					$scope.out = "Success.";
				})
				.error(function(data){
					print("Add error!");
					$scope.out = "Error occured!";
				});
		}
		catch(err){
			print("Error: "+err);
			$scope.out = err;
		}
	}
	 
	/**
	* Method pings all the hosts every 10 seconds and 
	* update the table with new response times
	*/
	ping = $interval(function(){
		if($scope.hosts.length==0) {
			console.log("List empty!");
			return;
		}
		print("pinging...");
		var AllHosts = [];
		angular.forEach($scope.hosts,function(host){
			AllHosts.push(host.name);
		});
		
		//requests the server to ping and return response times
		$http.post('/ping-host',AllHosts.toString())
			.success(function(data){
				// console.log(data);
				angular.forEach($scope.hosts,function(host){
					for(var key in data){
						if(key==host.name){
							var time = data[key];
							delete data[key];//delete this key to ignore another comparison
							if(!time) time = "NoResponse";
							host.time = time;
						}
					}
				})
				print("Ping successful.");
			})
			.error(function(){
				print("Ping error!");
			});
	},10000);
	
	print = function(msg){
		var date = new Date();
		var time = date.getDate() + "/"
			        + (date.getMonth()+1)  + "/" 
			        + date.getFullYear() + " @ "  
			        + date.getHours() + ":"  
			        + date.getMinutes() + ":" 
			        + date.getSeconds();
		console.log("["+time+"]	"+msg);
	}
 
});