package com.hust.edu.vn.documentsystem.data;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AutoImportDataForTest implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ReviewSubjectRepository reviewSubjectRepository;

    @Autowired
    private ReviewTeacherRepository reviewTeacherRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        // Tạo và insert 20 user vào database
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password");
            user.setDob(new Date());
            user.setAvatar("avatar");
            user.setPhoneNumber("0123456789");
            user.setRoleType(i == 1 ? RoleType.ADMIN : RoleType.USER);
            user.setRootPath(UUID.randomUUID().toString());
            user.setCreatedAt(new Date());
            user.setEnable(i == 1 || Math.random() >= 0.5); // Sinh ngẫu nhiên giá trị enable
            users.add(user);
        }
        userRepository.saveAll(users);

        // Import 50 subjects
        List<Subject> subjects = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Subject subject = new Subject();
            subject.setName("Subject " + i);
            subject.setSubjectId("SUB" + i);
            subject.setDescription("Description " + i);
            subject.setCreatedAt(new Date());
            subject.setCourseCode("CODE" + i);

            // Random owner for subject
            int randomIndex = new Random().nextInt(users.size());
            User owner = users.get(randomIndex);
            subject.setOwner(owner);

            subjects.add(subject);
        }
        subjectRepository.saveAll(subjects);

        // Import 20 subjects
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String name = "Teacher " + i;
            String email = "teacher" + i + "@example.com";
            String phoneNumber = "0123456789";
            String description = "Description for teacher " + i;

            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setEmail(email);
            teacher.setPhoneNumber(phoneNumber);
            teacher.setDescription(description);
            teacher.setDob(new Date());
            teacher.setCreatedAt(new Date());
            teacher.setAvatar("https://example.com/teacher/avatar/" + i);
            int randomIndex = new Random().nextInt(users.size());
            teacher.setOwner(users.get(randomIndex));

            teachers.add(teacher);
        }
        teacherRepository.saveAll(teachers);


        // Import 50 document
        String[] docTypes = {"pdf", "ppt", "png", "docx", "doc"};
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User user = userRepository.findById((long) (i % 20 + 1)).orElse(null);
            if (user != null) {
                Document document = new Document();
                document.setContent("Content of document " + i);
                document.setContentEn("Content of document " + i + " in English");
                document.setType(DocumentType.POST_DOCUMENT);
                document.setPath(UUID.randomUUID().toString() + "." + docTypes[i % 3]);
                document.setCreatedAt(new Date());
                document.setName("Document " + i + "." + docTypes[i % 5]);
                document.setContentType("application/" + docTypes[i % 5]);
                documents.add(document);
            }
        }
        documentRepository.saveAll(documents);


        // Imports 40 review subject
        Random rand = new Random();
        List<String> reviews = Arrays.asList("<h1>Chào các bạn hôm nay tôi sẽ review về giảng viên ABC</h1><strong>cách giảng dạy,...</strong>",
                "<h1>Review về môn Giải tích</h1><p>Thầy cô rất tận tâm và nhiệt tình trong giảng dạy</p>",
                "<h1>Đánh giá về môn Đại số</h1><p>Cô giảng bài rất dễ hiểu và cẩn thận trong từng chi tiết</p>",
                "<h1>Review về giảng viên XYZ</h1><p>Thầy cô rất thông minh và giàu kinh nghiệm</p>");
        List<ReviewSubject> reviewSubjects = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ReviewSubject review = new ReviewSubject();
            review.setReview(reviews.get(rand.nextInt(reviews.size())));
            review.setDone(rand.nextBoolean());
            review.setHidden(false);
            review.setApproved(ApproveType.NEW);
            review.setOwner(users.get(rand.nextInt(users.size())));
            review.setSubject(subjects.get(rand.nextInt(subjects.size())));
            reviewSubjects.add(review);
        }
        reviewSubjectRepository.saveAll(reviewSubjects);

        // Imports 40 review teachers
        List<ReviewTeacher> reviewTeachers = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ReviewTeacher reviewTeacher = new ReviewTeacher();
            reviewTeacher.setReview("<h1>Giảng viên này rất giỏi</h1><p>Tôi rất hài lòng về cách giảng dạy của giảng viên này</p>");
            reviewTeacher.setDone(false);
            reviewTeacher.setOwner(users.get(rand.nextInt(users.size())));
            reviewTeacher.setTeacher(teachers.get(rand.nextInt(teachers.size())));
            reviewTeacher.setCreatedAt(new Date());
            reviewTeacher.setHidden(rand.nextBoolean());
            reviewTeacher.setApproved(ApproveType.NEW);
            reviewTeachers.add(reviewTeacher);
        }
        reviewTeacherRepository.saveAll(reviewTeachers);

        // Imports 50 post
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User owner = users.get(rand.nextInt(users.size()));
            Subject subject = subjects.get(rand.nextInt(subjects.size()));
            String description = "Description for post " + i;
            String descriptionEn = "Description for post " + i + " in English";
            String content = "<div id=\"question1\">\n" +
                    "<strong>Bài 1:</strong> Cho hàm số y = x<sup>2</sup> - 2x + 1.\n" +
                    "<ul>\n" +
                    "<li>Tìm giá trị nhỏ nhất của hàm số.</li>\n" +
                    "<li>Tìm giá trị lớn nhất của hàm số trên đoạn [-1, 2].</li>\n" +
                    "</ul>\n" +
                    "</div>";
            String contentEn = "<div id=\"question1\">\n" +
                    "<strong>Exercise 1:</strong> Let y = x<sup>2</sup> - 2x + 1 be a function.\n" +
                    "<ul>\n" +
                    "<li>Find the minimum value of the function.</li>\n" +
                    "<li>Find the maximum value of the function on the interval [-1, 2].</li>\n" +
                    "</ul>\n" +
                    "</div>";

            Post post = new Post();
            post.setOwner(owner);
            post.setDescription(description);
            post.setDescriptionEn(descriptionEn);
            post.setContent(content);
            post.setContentEn(contentEn);
            post.setDocument(documents.get(i));
            post.setSubject(subject);
           posts.add(post);
        }
        postRepository.saveAll(posts);

    }
}