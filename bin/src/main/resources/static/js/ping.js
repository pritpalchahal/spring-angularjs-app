angular.module('ping', [])
  .controller('mainCtrl', function($scope, $http, $interval) {
	$scope.hosts = [];
	ValidHostnameRegex = /^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9])$/;
	
	/**
	* Method takes a host and flip its isSelected property
	*/
	$scope.select = function(host){
		host.isSelected = !host.isSelected;
	}
	
	/**
	* Method removes selected hosts from the list
	*/
	$scope.delete = function(){
		for(var i=0;i<$scope.hosts.length;i++){
			if($scope.hosts[i].isSelected){
				$scope.hosts.splice(i,1);
				i--;
			}
		}
	}
	
	/**
	* Method adds a new host to the list
	*/
	$scope.addHost = function(){
		var host_name = $scope.host.toLowerCase();
		$scope.host = "";
		//check if the hostname is valid
		if(!ValidHostnameRegex.test(host_name)){
			console.log("Invalid hostname !!!");    
			alert("Invalid hostname !!!");
			return;
		}
		for(var i=0;i<$scope.hosts.length;i++){
			//ignore duplicate host names
			if($scope.hosts[i].name == host_name){
				console.log("Hostname already exists!");
				alert("Hostname already exists!");
				return;
			}
		}
		var obj = {"name":host_name,"time":null,"isSelected":false};
		$scope.hosts.push(obj);
	}
	 
	/**
	* Method pings all the hosts every 10 seconds and 
	* update the table with new response times
	*/
	ping = $interval(function(){
		console.log("ping started...");
		if($scope.hosts.length==0) {
			console.log("List empty!");
			alert("List empty!");
			return;
		}
		var selectedHosts = [];
		angular.forEach($scope.hosts,function(host){
			// if(host.isSelected){
				selectedHosts.push(host.name);
			// }
		});
		
		//requests the server to ping and return response times
		$http.post('/ping-host',selectedHosts.toString())
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
				console.log("Ping successful.");
			})
			.error(function(){
				console.log("Ping error!");
			});
	},10000);
 
});