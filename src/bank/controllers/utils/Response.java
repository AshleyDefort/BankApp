package bank.controllers.utils;

public class Response {
  private String message;
  private Object data;
  private int status;

  public Response(String message, int status) {
    this.message = message;
    this.status = status;
  }

  public Response(String message, Object data, int status) {
    this.message = message;
    this.data = data;
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public Object getData() {
    return data;
  }

  public int getStatus() {
    return status;
  }
}
