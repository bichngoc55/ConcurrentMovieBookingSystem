  <a href="https://www.uit.edu.vn/" title="Trường Đại học Công nghệ Thông tin" target="_blank">
                <img src="https://www.uit.edu.vn/sites/vi/files/banner_uit_15.png" alt="UIT Banner">
            </a>
        </div>
        <h1>HỆ THỐNG BÁN VÉ RẠP CHIẾU PHIM VỚI LẬP TRÌNH ĐỒNG THỜI (CONCURRENCY) TRONG JAVA</h1>
        <p>Hệ thống bán vé rạp chiếu phim sử dụng lập trình đồng thời trong Java, được thiết kế để xử lý nhiều yêu cầu bán vé đồng thời từ nhiều khách hàng mà không gây ra xung đột hoặc sai lệch dữ liệu. Hệ thống đảm bảo hiệu suất và tính ổn định khi xử lý nhiều luồng (threads) đồng thời, ngăn chặn tình trạng bán trùng vé cho cùng một chỗ ngồi.</p>
        <div class="table-of-contents">
            <h2>Table of Contents</h2>
            <ul>
                <li><a href="#features">Tính Năng</a></li>
                <li><a href="#technology">Công Nghệ</a></li> 
                <li><a href="#course-info">Thông Tin Môn Học</a></li>
                <li><a href="#team-members">Thành Viên Nhóm</a></li>
            </ul>
        </div>
        <section id="features">
            <h2><a name="features"></a>Tính Năng</h2>
            <p><strong>Quản lý phim và suất chiếu:</strong></p>
            <ul>
                <li>Danh sách phim có sẵn và suất chiếu</li>
                <li>Thêm, xóa, cập nhật thông tin phim và suất chiếu</li>
            </ul>
            <p><strong>Chọn và đặt vé:</strong></p>
            <ul>
                <li>Chọn phim, suất chiếu</li>
                <li>Chọn ghế ngồi từ sơ đồ hiển thị ghế trống</li>
                <li>Đảm bảo không xảy ra xung đột khi nhiều người dùng chọn cùng một ghế</li>
            </ul>
            <p><strong>Quản lý tình trạng ghế:</strong></p>
            <ul>
                <li>Đặt vé và cập nhật trạng thái ghế</li>
                <li>Hủy vé và trả ghế về trạng thái trống</li>
                <li>Đồng bộ hóa thay đổi trạng thái ghế giữa các luồng</li>
            </ul>
            <p><strong>Xử lý đa luồng:</strong></p>
            <ul>
                <li>Xử lý nhiều yêu cầu đồng thời từ khách hàng</li>
                <li>Ngăn chặn race condition và deadlock</li>
                <li>Đảm bảo tính nhất quán của dữ liệu</li>
            </ul>
        </section>
        <section id="technology">
            <h2><a name="technology"></a>Công Nghệ</h2>
            <ul>
                <li><strong>Java:</strong> Ngôn ngữ lập trình chính</li>
                <li><strong>Spring Boot:</strong> Framework phát triển ứng dụng</li>
                <li><strong>Thymeleaf:</strong> Template engine cho giao diện người dùng</li>
                <li><strong>Java Concurrency API:</strong> Đồng bộ hóa và xử lý đa luồng
                    <ul>
                        <li>ExecutorService</li>
                        <li>ReentrantLock</li>
                        <li>AtomicBoolean/AtomicInteger</li>
                        <li>ConcurrentHashMap</li>
                        <li>Synchronized methods</li>
                    </ul>
                </li>
            </ul>
        </section>
# <a name="ThongTin">Thông Tin Môn Học</a>

| Môn Học        | Các phương pháp lập trình |
| -------------- | -------------------- |
| Lớp            | SE334.P21            |
| GV Lý Thuyết   | Ths. Nguyễn Duy Khánh|

# <a name="contribution">Thông Tin Thành Viên</a>

| MSSV       | Họ và Tên          | Email                   | Github                                                                                                                      |
| ---------- | ------------------ | ----------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| `22520957` | Nguyễn Thị Bích Ngọc| 222520957@gm.uit.edu.vn | [![](https://img.shields.io/badge/bichngoc-%2324292f.svg?style=flat-square&logo=github      )](https://github.com/bichngoc55) |
| `22521644` | Đặng Thị Bảo Linh| 22520757@gm.uit.edu.vn | [![](https://img.shields.io/badge/baolinh-%2324292f.svg?style=flat-square&logo=github      )](https://github.com/hniloablingg) |
