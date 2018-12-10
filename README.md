# culebao-java-sdk
For application connect fabric

### 1. Setup the Blockchain Network

[Clone this repo](https://github.com/IBM/blockchain-application-using-fabric-java-sdk) using the following command.

```
$ git clone https://github.com/IBM/blockchain-application-using-fabric-java-sdk.git
```

To build the blockchain network, the first step is to generate artifacts for peers and channels using cryptogen and configtx. The utilities used and steps to generate artifacts are explained [here](http://hyperledger-fabric.readthedocs.io/en/release-1.1/build_network.html). In this pattern all required artifacts for the peers and channel of the network are already generated and provided to use as-is. Artifacts can be located at:

   ```
   network_resources/crypto-config
   network_resources/config
   ````

The automated scripts to build the network are provided under `network` directory. The `network/docker-compose.yaml` file defines the blockchain network topology. This pattern provisions a Hyperledger Fabric 1.1 network consisting of two organizations, each maintaining two peer node, two certificate authorities for each organization and a solo ordering service. Need to run the script as follows to build the network.

> **Note:** Please clean up the old docker images (if any) from your environment otherwise you may get errors while setting up network.

   ```
   cd network
   chmod +x build.sh
   ./build.sh
   ```

To stop the running network, run the following script.

   ```
   cd network
   chmod +x stop.sh
   ./stop.sh
   ```

To delete the network completely, following script need to execute.

   ```
   cd network
   chmod +x teardown.sh
   ./teardown.sh
   ```

### 2. Build the client based on Fabric Java SDK

The previous step creates all required docker images with the appropriate configuration.

**Java Client**
* The java client sources are present in the folder `java` of the repo.
* Check your environment before executing the next step. Make sure, you are able to run `mvn` commands properly.
   > If `mvn` commands fails, please refer to [Pre-requisites](https://hyperledger.fabric.io) to install maven.


To work with the deployed network using Hyperledger Fabric SDK java 1.0.0, perform the following steps.

* Open a command terminal and navigate to the `java` directory in the repo. Run the command `mvn install`.

   ```
   cd ../java
   mvn install
   ```

* A jar file `blockchain-java-sdk-0.0.1-SNAPSHOT-jar-with-dependencies.jar` is built and can be found under the `target` folder. This jar can be renamed to `blockchain-client.jar` to keep the name short. 

   ```
   cd target
   cp blockchain-java-sdk-0.0.1-SNAPSHOT-jar-with-dependencies.jar blockchain-client.jar
   ```
   
* Copy this built jar into `network_resources` directory. This is required as the java code can access required artifacts during execution.

   ```
   cp blockchain-client.jar ../../network_resources
   ```

### 3. Create and Initialize the channel

In this code pattern, we create one channel `mychannel` which is joined by all four peers. The java source code can be seen at  `src/main/java/org/app/network/CreateChannel.java`. To create and initialize the channel, run the following command.

   ```
   cd ../../network_resources
   java -cp blockchain-client.jar main.java.org.app.network.CreateChannel
   ```

Output:

   ```Apr 20, 2018 5:11:42 PM org.app.util.Util deleteDirectory
      INFO: Deleting - users
      Apr 20, 2018 5:11:45 PM org.app.network.CreateChannel main
      INFO: Channel created mychannel
      Apr 20, 2018 5:11:45 PM org.app.network.CreateChannel main
      INFO: peer0.org1.example.com at grpc://localhost:7051
      Apr 20, 2018 5:11:45 PM org.app.network.CreateChannel main
      INFO: peer1.org1.example.com at grpc://localhost:7056
      Apr 20, 2018 5:11:45 PM org.app.network.CreateChannel main
      INFO: peer0.org2.example.com at grpc://localhost:8051
      Apr 20, 2018 5:11:45 PM org.app.network.CreateChannel main
      INFO: peer1.org2.example.com at grpc://localhost:8056
   ```  
   
### 4. Deploy and Instantiate the chaincode

This code pattern uses a sample chaincode `fabcar` to demo the usage of Hyperledger Fabric SDK Java APIs. To deploy and instantiate the chaincode, execute the following command.

   ```
   java -cp blockchain-client.jar main.java.org.app.network.DeployInstantiateChaincode
   ```
   
   Output:
   
   ```INFO: Deploying chaincode PSB_Custody using Fabric client Org1MSP admin
Dec 07, 2018 9:17:08 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code deployment SUCCESS
Dec 07, 2018 9:17:08 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code deployment SUCCESS
Dec 07, 2018 9:17:08 AM main.java.org.app.client.FabricClient deployChainCode
INFO: Deploying chaincode PSB_Custody using Fabric client Org2MSP admin
Dec 07, 2018 9:17:08 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code deployment SUCCESS
Dec 07, 2018 9:17:08 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code deployment SUCCESS
Dec 07, 2018 9:17:08 AM main.java.org.app.client.ChannelClient instantiateChainCode
INFO: Instantiate proposal request PSB_Custody on channel mychannel with Fabric client Org2MSP admin
Dec 07, 2018 9:17:08 AM main.java.org.app.client.ChannelClient instantiateChainCode
INFO: Instantiating Chaincode ID PSB_Custody on channel mychannel
Dec 07, 2018 9:17:10 AM main.java.org.app.client.ChannelClient instantiateChainCode
INFO: Chaincode PSB_Custody on channel mychannel instantiation java.util.concurrent.CompletableFuture@48bb62[Not completed]
Dec 07, 2018 9:17:10 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code instantiation SUCCESS
Dec 07, 2018 9:17:10 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code instantiation SUCCESS
Dec 07, 2018 9:17:10 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code instantiation SUCCESS
Dec 07, 2018 9:17:10 AM main.java.org.app.network.DeployInstantiateChaincode main
INFO: PSB_Custody- Chain code instantiation SUCCESS

   ```
   
   > **Note:** The chaincode fabcar.go was taken from the fabric samples available at - https://github.com/hyperledger/fabric-samples/tree/release-1.1/chaincode/fabcar/go.

### 5. Register and enroll users

A new user can be registered and enrolled to an MSP. Execute the below command to register a new user and enroll to Org1MSP.

   ```
   java -cp blockchain-client.jar main.java.org.app.user.RegisterEnrollUser
   ```
   
   Output:
   
   ```Apr 23, 2018 10:26:34 AM org.app.util.Util deleteDirectory
      INFO: Deleting - users
      log4j:WARN No appenders could be found for logger (org.hyperledger.fabric.sdk.helper.Config).
      log4j:WARN Please initialize the log4j system properly.
      log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
      Apr 23, 2018 10:26:35 AM org.app.client.CAClient enrollAdminUser
      INFO: CA -http://localhost:7054 Enrolled Admin.
      Apr 23, 2018 10:26:35 AM org.app.client.CAClient registerUser
      INFO: CA -http://localhost:7054 Registered User - user1524459395783
      Apr 23, 2018 10:26:36 AM org.app.client.CAClient enrollUser
      INFO: CA -http://localhost:7054 Enrolled User - user1524459395783
   ```

### 6. Perform Invoke and Query on network

Blockchain network has been setup completely and is ready to use. Now we can test the network by performing invoke and query on the network. The `fabcar` chaincode allows us to create a new asset which is a car. For test purpose, invoke operation is performed to create a new asset in the network and query operation is performed to list the asset of the network. Perform the following steps to check the same.

   ```
   java -cp blockchain-client.jar main.java.org.app.chaincode.invocation.InvokeChaincode
   ```

   Output:
   
   ```Dec 07, 2018 9:55:19 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - admin.ser
Dec 07, 2018 9:55:19 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - org1
Dec 07, 2018 9:55:19 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - users
log4j:WARN No appenders could be found for logger (org.hyperledger.fabric.sdk.helper.Config).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Dec 07, 2018 9:55:20 AM main.java.org.app.client.CAClient enrollAdminUser
INFO: CA -http://localhost:7054 Enrolled Admin.
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: rule:{ "ruleId":"RULE01", "ruleName":"testruleRULE01", "ruleDetail":" This is a test rule.", "ruleDate":20180508, "dataType":"rule"}
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: ruleBean:main.java.org.app.culebao.entity.Rule@59d2400d
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: ruleData:main.java.org.app.entity.Data@542e560f
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: msgBean:main.java.org.app.entity.Message@1d730606
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: arguments:[Ljava.lang.String;@5911e990
Dec 07, 2018 9:55:20 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: Sending transaction proposal on channel mychannel
Dec 07, 2018 9:55:20 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: Transaction proposal on channel mychannel OK SUCCESS with transaction id:6d3e8e77c12eaaf10767240a8e7007f8d91568897336f2ba8d12ca0bfee0122d
Dec 07, 2018 9:55:20 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: {"tranCode":"AddRule","tranDate":"","retCode":"00","data":null,"error":null}
Dec 07, 2018 9:55:20 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: java.util.concurrent.CompletableFuture@2eced48b[Not completed]
Dec 07, 2018 9:55:20 AM main.java.org.app.chaincode.invocation.InvokeChaincode main
INFO: Invoked createCar on PSB_Custody. Status - SUCCESS

  ```   

   ```
   java -cp blockchain-client.jar main.java.org.app.chaincode.invocation.QueryChaincode
   ```
   
   Output:
   
   ```
   Dec 07, 2018 10:20:41 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - admin.ser
Dec 07, 2018 10:20:41 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - org1
Dec 07, 2018 10:20:41 AM main.java.org.app.util.Util deleteDirectory
INFO: Deleting - users
log4j:WARN No appenders could be found for logger (org.hyperledger.fabric.sdk.helper.Config).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Dec 07, 2018 10:20:41 AM main.java.org.app.client.CAClient enrollAdminUser
INFO: CA -http://localhost:7054 Enrolled Admin.
Dec 07, 2018 10:20:42 AM main.java.org.app.chaincode.invocation.QueryChaincode main
INFO: msgBean:main.java.org.app.entity.Message@31920ade
Dec 07, 2018 10:20:42 AM main.java.org.app.chaincode.invocation.QueryChaincode main
INFO: arguments:[Ljava.lang.String;@604f2bd2
Dec 07, 2018 10:20:42 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: Sending transaction proposal on channel mychannel
Dec 07, 2018 10:20:42 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: Transaction proposal on channel mychannel OK SUCCESS with transaction id:c301ed2605b80ae770c3d4ded3b0fabf5f01cd518c23e9cb86b6e2c8542e09fb
Dec 07, 2018 10:20:42 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: {"tranCode":"SelectRuleHistory","tranDate":"","retCode":"00","data":[{"content":"{\"ruleId\":\"RULE01\",\"ruleName\":\"testruleRULE01\",\"ruleDetail\":\" This is a test rule.\",\"ruleDate\":20180508,\"dataType\":\"rule\"}","dataType":"Rule"}],"error":null}
Dec 07, 2018 10:20:42 AM main.java.org.app.client.ChannelClient sendTransactionProposal
INFO: java.util.concurrent.CompletableFuture@3569fc08[Not completed]
Dec 07, 2018 10:20:42 AM main.java.org.app.chaincode.invocation.QueryChaincode main
INFO: Query Rule History PSB_Custody. Status - SUCCESS

   ```
