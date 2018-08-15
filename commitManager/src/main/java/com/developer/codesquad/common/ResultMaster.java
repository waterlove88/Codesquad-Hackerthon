package com.developer.codesquad.common;

public class ResultMaster {

	private ResultCode result;
	private Object body;
	
	public ResultMaster(String code, String message) {
		result = new ResultCode(code, message);
	}
	
	public ResultCode getResult() {
		return result;
	}

	public Object getBody() {
		return body;
	}
	
	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ResultMaster [result=(" + result.toString() + "), body=" + body + "]";
	}

	public class ResultCode {
		private String code;
		private String message;
		
		public ResultCode(String code, String message) {
			this.code = code;
			this.message = message;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getMessage() {
			return message;
		}

		@Override
		public String toString() {
			return "ResultCode [code=" + code + ", message=" + message + "]";
		}
	}
}
