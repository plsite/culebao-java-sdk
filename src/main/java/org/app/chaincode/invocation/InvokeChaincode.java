/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */ 
package main.java.org.app.chaincode.invocation;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.org.app.client.CAClient;
import main.java.org.app.client.ChannelClient;
import main.java.org.app.client.FabricClient;
import main.java.org.app.config.Config;
import main.java.org.app.culebao.entity.Activity;
import main.java.org.app.culebao.entity.Rule;
import main.java.org.app.culebao.entity.Transaction;
import main.java.org.app.entity.Data;
import main.java.org.app.entity.Message;
import main.java.org.app.user.UserContext;
import main.java.org.app.util.JacksonUtil;
import main.java.org.app.util.Util;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class InvokeChaincode {

	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
	private static final String EXPECTED_EVENT_NAME = "event";
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String args[]) {
//		addRule();
		recordTransaction();
	}
	
	public static void createActivity() {
		try {
            Util.cleanUp();
			String caUrl = Config.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
			
			FabricClient fabClient = new FabricClient(adminUserContext);
			
			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("Invoke");
			/*
			 * Add Activity
			 * {
			 * 	"activityId":"ID",
			 * 	"activityName":"NAME",
			 * 
			 * 	"bodyId":"FID",
			 * 	"bodyName":"FNAME",
			 * 	"bodyOrgId":"",
			 * 	"bodyOrgName":"",
			 * 	"bodyDepartmentId":"",
			 * 	"bodyDepartmentName":"",
			 * 
			 * 	"deviceId":"",
			 * 	"deviceBrand":"",
			 * 	"deviceModel":"",
			 * 	
			 * 	"activityDate":,
			 * 	"activityScore":""
			 * 
			 * 	"scoreRules":ActivityRule[],
			 * 	
			 * 	"ownerOrgId":"",
			 * 	"ownerOrgName":"",
			 * 
			 * 	"businessType":"",
			 * 	
			 * 	"activityDescribe":"",
			 * 	"comments":"",
			 * 	
			 * 	"dataType":""
			 * 
			 * }
			 */
			String activityId = "ACT01";
			Activity activity = new Activity();
			activity.setActivityId(activityId);
			activity.setActivityName("");
			activity.setBodyId("");
			activity.setBodyName("");
			activity.setBodyOrgId("");
			activity.setBodyOrgName("");
			activity.setBodyDepartmentId("");
			activity.setBodyDepartmentName("");
			activity.setDeviceId("");
			activity.setDeviceBrand("");
			activity.setDeviceModel("");
			activity.setActivityDate(20180901);
			activity.setActivityScore("");
			activity.setScoreRules(null);
			activity.setOwnerOrgId("");
			activity.setOwnerOrgName("");
			activity.setBusinessType("");
			activity.setActivityDescribe("");
			activity.setComments("");
			activity.setDataType("");
			
	        String activityJson = "{ \"activityId\":\"" + activity.getActivityId() + "\"," + " \"activityName\":\" " + activity.getActivityName() + "\"," + " \"bodyId\":"+ activity.getBodyId() +"," + " \"bodyName\":"+ activity.getBodyName() +","+ " \"bodyOrgId\":"+ activity.getBodyOrgId()+","+" \"bodyOrgName\":"+ activity.getBodyOrgName() +"," +" \"bodyDepartmentId\":"+ activity.getBodyDepartmentId() +","+" \"bodyDepartmentName\":"+ activity.getBodyDepartmentName() +"," +" \"deviceId\":"+ activity.getDeviceId() +","+" \"deviceBrand\":"+ activity.getDeviceBrand() +","+" \"deviceModel\":"+ activity.getDeviceModel() +","+" \"ativityDate\":"+ activity.getActivityDate() +","+ " \"activityScore\":"+ activity.getActivityScore() +","+ " \"scoreRules\":"+ activity.getScoreRules() +","+ " \"ownerOrgId\":"+ activity.getOwnerOrgId() +","+" \"ownerOrgName\":"+ activity.getOwnerOrgName() +","+" \"businessType\":"+ activity.getBusinessType() +","+" \"activityDescribe\":"+ activity.getActivityDescribe() +","+ " \"comments\":"+ activity.getComments() +","+ " \"dataType\":"+ activity.getDataType() + "}";
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"activity:"+activityJson.toString());

	        Activity activityBean = (Activity)JacksonUtil.readValue(activityJson, Activity.class);
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"activity:"+activityBean.toString());

			Data activityData = new Data();
			activityData.setDataType("tranSaction");
			activityData.setContent(JacksonUtil.toJSON(activityBean));
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"activity:"+activityData.toString());
			
			Message msgBean = new Message();
			msgBean.setChannel("mychannel");
			msgBean.setTranCode("RecordTransaction");
			msgBean.setTranDate(sdf.format(new Date()));
			msgBean.setData(new Data[] {activityData});
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"msgBean:"+msgBean.toString());
			
			String argumentJson = JacksonUtil.toJSON(msgBean);
			String[] arguments = new String[] {argumentJson};
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"arguments:"+arguments.toString());

			
			
			request.setArgs(arguments);
			request.setProposalWaitTime(1000);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); 																								
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); 
			tm2.put("result", ":)".getBytes(UTF_8));
			tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA); 
			request.setTransientMap(tm2);
			Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
			for (ProposalResponse res: responses) {
				Status status = res.getStatus();
				Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"Invoked addActivity "+Config.CHAINCODE_1_NAME + ". Status - " + status);
			}
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * add transaction
	 */
	public static void recordTransaction() {
		try {
            Util.cleanUp();
			String caUrl = Config.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
			
			FabricClient fabClient = new FabricClient(adminUserContext);
			
			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("Invoke");
			/*
			 * Add Transaction
			 * {
			 * 	"transactionId":"ID",
			 * 	"transactionName":"NAME",
			 * 	"fromId":"FID",
			 * 	"fromName":"FNAME",
			 * 	"toId":"TID",
			 * 	"toName":"TNAME",
			 * 	"date":20181201,
			 * 	"transactionCount":1,
			 * 	"transactionType":"debit",
			 * 	""
			 * 
			 * 
			 * }
			 */
			String tranSactionId = "TRANSACTION01";
	        String tranSaction = "{ \"transactionId\":\"" + tranSactionId + "\"," + " \"transactionName\":\"testrule" + tranSactionId + "\"," + " \"fromId\":\"ORG01\"," + "\"fromName\":\"Culebao\"," + "\"toId\":\"USER01\"," + "\"toName\":\"James\"," + " \"date\":20181008," + "\"transactionCount\":50," + "\"transactionType\":\"debit\"," + "\"activityId\":\"ACT01\"," + "\"activityName\":\"TestACT01\"," + "\"conversionRate\":\"1:1\"," + "\"pointCount\":50," + "\"comments\":\"this is a test transaction\"," + " \"dataType\":\"transaction\"" + "}";
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"transaction:"+tranSaction.toString());

	        Transaction tranSactionBean = (Transaction)JacksonUtil.readValue(tranSaction, Transaction.class);
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"transaction:"+tranSactionBean.toString());

			Data tranSactionData = new Data();
			tranSactionData.setDataType("tranSaction");
			tranSactionData.setContent(JacksonUtil.toJSON(tranSactionBean));
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"transactionData:"+tranSactionBean.toString());
			
			Message msgBean = new Message();
			msgBean.setChannel("mychannel");
			msgBean.setTranCode("RecordTransaction");
			msgBean.setTranDate(sdf.format(new Date()));
			msgBean.setData(new Data[] {tranSactionData});
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"msgBean:"+msgBean.toString());
			
			String argumentJson = JacksonUtil.toJSON(msgBean);
			String[] arguments = new String[] {argumentJson};
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"arguments:"+arguments.toString());

			
			
			request.setArgs(arguments);
			request.setProposalWaitTime(1000);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); 																								
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); 
			tm2.put("result", ":)".getBytes(UTF_8));
			tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA); 
			request.setTransientMap(tm2);
			Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
			for (ProposalResponse res: responses) {
				Status status = res.getStatus();
				Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"Invoked addTransaction "+Config.CHAINCODE_1_NAME + ". Status - " + status);
			}
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * add rule
	 */
	public static void addRule() {
		try {
            Util.cleanUp();
			String caUrl = Config.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
			
			FabricClient fabClient = new FabricClient(adminUserContext);
			
			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("Invoke");
//			String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
			/*
			 * CreateRule
			 */
			String ruleId = "RULE04";
	        String rule = "{ \"ruleId\":\"" + ruleId + "\"," + " \"ruleName\":\"testrule" + ruleId + "\"," + " \"ruleDetail\":\" This is a test rule.\"," + " \"ruleDate\":20180508," + " \"dataType\":\"rule\"" + "}";
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"rule:"+rule.toString());

	        Rule ruleBean = (Rule)JacksonUtil.readValue(rule, Rule.class);
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"ruleBean:"+ruleBean.toString());

			Data ruleData = new Data();
			ruleData.setDataType("rule");
			ruleData.setContent(JacksonUtil.toJSON(ruleBean));
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"ruleData:"+ruleData.toString());
			
			Message msgBean = new Message();
			msgBean.setChannel("mychannel");
			msgBean.setTranCode("AddRule");
			msgBean.setTranDate(sdf.format(new Date()));
			msgBean.setData(new Data[] {ruleData});
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"msgBean:"+msgBean.toString());
			
			String argumentJson = JacksonUtil.toJSON(msgBean);
			String[] arguments = new String[] {argumentJson};
			Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"arguments:"+arguments.toString());

			
			
			request.setArgs(arguments);
			request.setProposalWaitTime(1000);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); 																								
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); 
			tm2.put("result", ":)".getBytes(UTF_8));
			tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA); 
			request.setTransientMap(tm2);
			Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
			for (ProposalResponse res: responses) {
				Status status = res.getStatus();
				Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"Invoked addRule "+Config.CHAINCODE_1_NAME + ". Status - " + status);
			}
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
