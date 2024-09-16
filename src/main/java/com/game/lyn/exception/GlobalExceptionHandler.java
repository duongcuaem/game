package com.game.lyn.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Xử lý tất cả ngoại lệ dạng CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> handleCustomException(CustomException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), ex.getDetail(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    // Xử lý ngoại lệ chung (Exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getLocalizedMessage(), "Unexpected error occurred", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

// Định nghĩa các trạng thái
// // Phản hồi thông tin (1xx)
// CONTINUE(100, Series.INFORMATIONAL, "Continue"),
//    -> Sử dụng khi client đã gửi một phần request và server báo rằng đã nhận được. Client có thể tiếp tục gửi phần tiếp theo của request (thường dùng trong truyền tải dữ liệu lớn).

// SWITCHING_PROTOCOLS(101, Series.INFORMATIONAL, "Switching Protocols"),
//    -> Sử dụng khi server đồng ý chuyển đổi giao thức theo yêu cầu của client (ví dụ, từ HTTP sang WebSocket).

// PROCESSING(102, Series.INFORMATIONAL, "Processing"),
//    -> Sử dụng khi server đã nhận request nhưng vẫn đang xử lý. Thường được dùng trong các hệ thống đòi hỏi thời gian xử lý dài (như WebDAV).

// // Phản hồi thành công (2xx)
// OK(200, Series.SUCCESSFUL, "OK"),
//    -> Sử dụng khi request đã thành công và server trả về dữ liệu mong muốn. Đây là mã phản hồi phổ biến nhất.

// CREATED(201, Series.SUCCESSFUL, "Created"),
//    -> Sử dụng khi một resource mới được tạo thành công (thường là sau khi POST). Ví dụ: tạo tài khoản hoặc thêm dữ liệu mới vào cơ sở dữ liệu.

// ACCEPTED(202, Series.SUCCESSFUL, "Accepted"),
//    -> Sử dụng khi server đã nhận request nhưng quá trình xử lý request chưa hoàn thành. Thường dùng trong các hệ thống xử lý không đồng bộ.

// // Thông báo chuyển hướng (3xx)
// MOVED_PERMANENTLY(301, Series.REDIRECTION, "Moved Permanently"),
//    -> Sử dụng khi một tài nguyên đã được di chuyển vĩnh viễn sang URL mới. Trình duyệt hoặc client nên cập nhật liên kết để trỏ tới URL mới.

// FOUND(302, Series.REDIRECTION, "Found"),
//    -> Sử dụng khi tài nguyên tạm thời được tìm thấy tại một URL khác, nhưng URL ban đầu vẫn sẽ được sử dụng cho các request trong tương lai.

// // Phản hồi lỗi từ phía client (4xx)
// BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
//    -> Sử dụng khi request từ phía client có lỗi hoặc không thể hiểu được (ví dụ: sai định dạng JSON, thiếu thông tin yêu cầu).

// UNAUTHORIZED(401, Series.CLIENT_ERROR, "Unauthorized"),
//    -> Sử dụng khi người dùng chưa được xác thực hoặc xác thực không hợp lệ. Thường yêu cầu client gửi lại request với thông tin đăng nhập chính xác.

// FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"),
//    -> Sử dụng khi người dùng đã xác thực nhưng không có quyền truy cập vào tài nguyên yêu cầu.

// NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found"),
//    -> Sử dụng khi server không thể tìm thấy tài nguyên yêu cầu. Đây là mã lỗi phổ biến khi URL không tồn tại.

// CONFLICT(409, Series.CLIENT_ERROR, "Conflict"),
//    -> Sử dụng khi xảy ra xung đột trong request (ví dụ: hai người dùng cố gắng cập nhật cùng một tài nguyên cùng lúc hoặc gửi dữ liệu bị trùng lặp).

// // Phản hồi lỗi từ phía server (5xx)
// INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"),
//    -> Sử dụng khi có lỗi từ phía server mà không xác định rõ được nguyên nhân. Đây là mã lỗi chung chung khi có vấn đề nghiêm trọng xảy ra.

// NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"),
//    -> Sử dụng khi server không hỗ trợ hoặc chưa được triển khai phương thức mà client yêu cầu.

// BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"),
//    -> Sử dụng khi server đóng vai trò proxy hoặc gateway và nhận được phản hồi không hợp lệ từ server upstream.

}
