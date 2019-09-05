package sms;

import model.request.Message;
import model.response.MessageModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

import log.Log;

public class SendMessageLMS {
	public static void sendLMS(Log log, String hospitalName) {
		/* 수신번호, 발신번호, 내용, 제목 순 */
		Message message = new Message("01031232396", "01031232396",
				"※응급환자발생※\n" + "이름:" + log.getAccountInfo().getAccountName().toString() + "\n" 
						+ "맥박:" + log.getPulse().toString() + "\n" 
						+ "체온:" + log.getTemp().toString() + "℃\n" 
						+ "발생위치:" + log.getLatitude().toString() + ", " + log.getLongtitude().toString() + "\n"
						+ "인근병원:" + hospitalName	+ "\n\n"
						+ "- from 구해줘 -",
				"PPLAS");

		Call<MessageModel> api = APIInit.getAPI().sendMessage(APIInit.getHeaders(), message);
		api.enqueue(new Callback<MessageModel>() {
			@Override
			public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
				// 성공 시 200이 출력됩니다.
				if (response.isSuccessful()) {
					System.out.println("statusCode : " + response.code());
					MessageModel body = response.body();
					System.out.println("groupId : " + body.getGroupId());
					System.out.println("messageId : " + body.getMessageId());
					System.out.println("to : " + body.getTo());
					System.out.println("from : " + body.getFrom());
					System.out.println("type : " + body.getType());
					System.out.println("statusCode : " + body.getStatusCode());
					System.out.println("statusMessage : " + body.getStatusMessage());
					System.out.println("customFields : " + body.getCustomFields());
				} else {
					try {
						System.out.println(response.errorBody().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Call<MessageModel> call, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
	}

	
}
