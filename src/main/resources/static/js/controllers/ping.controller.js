app.controller('PingController', function($scope, $http, $timeout, UserService, FlashService, $rootScope, $location) {
	$scope.hosts = [];
	$scope.user = null;
	var timer = null;

	loadCurrentUser();
	
    function loadCurrentUser() {
        UserService.GetByUsername($rootScope.globals.currentUser.username)
            .then(function (user) {
                $scope.user = user;
            });
    }
    
    $scope.logout = function(){
    	//stop the ping functionality
    	$timeout.cancel(timer);
    	//navigate back to login page
    	$location.path('/login');
    }
    
    //wrapper function to allow pinging every 10 seconds
    function wrapper(){
    	ping();
    	timer = $timeout(wrapper,10000);
    }
    timer = $timeout(wrapper,10000);
    
	/**
	*	Flips host selection.
	*/
	$scope.select = function(host){
		host.isSelected = !host.isSelected;
		if(host.isSelected){
			FlashService.Success("Selected "+host.name);
		}
		else{
			FlashService.Success("Deselected "+host.name);
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
				count++;
				$scope.hosts.splice(i,1);
				i--;
			}
		}
		FlashService.Success("Deleted "+count+ " hosts.");
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
			
			var host_name = $scope.host.toLowerCase();
			
			//check if name already exists
			angular.forEach($scope.hosts,function(host){
				if(host.name == host_name){
					throw host_name+" already exists!";
				}
			})
	
			//validate host name from server
			$http.post('/add-host',host_name)
				.then(function(data){
					var value = data.data;
					if(!value) {
						FlashService.Error("Invalid host!",false);
						return;
					}
					$scope.host = "";//clear the input text box
					var obj = {"name":host_name,"time":null,"isSelected":false};
					$scope.hosts.push(obj);
					FlashService.Success("Added successfully.");
				})
				.catch(function(data){
					print("Add error! "+data);
					FlashService.Error("Error at server!");
				});
		}
		catch(err){
			FlashService.Error(err.Message);
		}
	}
	
	/**
	* Method pings all the hosts every 10 seconds and 
	* update the table with new response times
	*/
	ping = function(){
		print("Pinging....");
		if($scope.hosts.length==0) {
			console.log("List empty!");
			return;
		}
		var AllHosts = [];
		angular.forEach($scope.hosts,function(host){
			AllHosts.push(host.name);
		});
		
		//requests the server to ping and return response times
		$http.post('/ping-host',AllHosts.toString())
			.then(function(data){
				var value = data.data;
				angular.forEach($scope.hosts,function(host){
					for(var key in value){
						if(key==host.name){
							var time = value[key];
							delete value[key];//delete this key to ignore another comparison
							if(!time) time = "NoResponse";
							host.time = time;
						}
					}
				})
			})
			.catch(function(err){
				FlashService.Error(err.data);
			});
	}
	
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