package com.hust.edu.vn.documentsystem.data;

import com.hust.edu.vn.documentsystem.common.type.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubjectDocumentRepository subjectDocumentRepository;

    @Autowired
    private CommentReviewSubjectRepository commentReviewSubjectRepository;

    @Autowired
    private CommentReviewTeacherRepository commentReviewTeacherRepository;

    @Autowired
    private FavoriteSubjectRepository favoriteSubjectRepository;

    @Autowired
    private AnswerPostRepository answerPostRepository;


    @Override
    public void run(String... args) throws Exception {
        // Tạo và insert 20 user vào database
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword(passwordEncoder.encode("password" + i));
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
        users = userRepository.findAll();
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
        subjects = subjectRepository.findAll();
        // Import 20 subjects
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
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
        teachers = teacherRepository.findAll();


        // Imports 40 review subject
        Random rand = new Random();
        List<String> reviews = Arrays.asList("<h1>Chào các bạn hôm nay tôi sẽ review về giảng viên ABC</h1><strong>cách giảng dạy,...</strong>",
                "<h1>Review về môn Giải tích</h1><p>Thầy cô rất tận tâm và nhiệt tình trong giảng dạy</p>",
                "<h1>Đánh giá về môn Đại số</h1><p>Cô giảng bài rất dễ hiểu và cẩn thận trong từng chi tiết</p>",
                "<h1>Review về giảng viên XYZ</h1><p>Thầy cô rất thông minh và giàu kinh nghiệm</p>");
        List<ReviewSubject> reviewSubjects = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ReviewSubject review = new ReviewSubject();
            review.setReview(reviews.get(rand.nextInt(reviews.size())));
            review.setDone(rand.nextBoolean());
            review.setHidden(false);
            review.setApproved(ApproveType.NEW);
            review.setOwner(users.get(i));
            review.setSubject(subjects.get(i));
            reviewSubjects.add(review);
        }
        reviewSubjectRepository.saveAll(reviewSubjects);
        reviewSubjects = reviewSubjectRepository.findAll();

        // Imports 40 review teachers
        List<ReviewTeacher> reviewTeachers = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ReviewTeacher reviewTeacher = new ReviewTeacher();
            reviewTeacher.setReview("<h1>Giảng viên này rất giỏi</h1><p>Tôi rất hài lòng về cách giảng dạy của giảng viên này</p>");
            reviewTeacher.setDone(false);
            reviewTeacher.setOwner(users.get(i));
            reviewTeacher.setTeacher(teachers.get(i));
            reviewTeacher.setCreatedAt(new Date());
            reviewTeacher.setHidden(rand.nextBoolean());
            reviewTeacher.setApproved(ApproveType.NEW);
            reviewTeachers.add(reviewTeacher);
        }
        reviewTeacherRepository.saveAll(reviewTeachers);
        reviewTeachers = reviewTeacherRepository.findAll();

        // Import 20 document for post
        String[] docTypes = {"pdf", "ppt", "png", "docx", "doc"};
        List<Document> documentForPosts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
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
                documentForPosts.add(document);
            }
        }
        documentRepository.saveAll(documentForPosts);
        documentForPosts = documentRepository.findAll();

        // Imports 40 post
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            User owner = users.get(rand.nextInt(users.size()));
            Subject subject = subjects.get(rand.nextInt(subjects.size()));
            Document document = documentForPosts.get(rand.nextInt(documentForPosts.size()));
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
            post.setContent(i >= 20 ? content : null);
            post.setContentEn(i >= 20 ? contentEn : null);
            post.setDocument(i < 20 ? document : null);
            post.setSubject(subject);
            posts.add(post);
        }
        postRepository.saveAll(posts);
        posts = postRepository.findAll();

        // Imports comment for post
        List<CommentPost> commentPosts = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            Post post = posts.get(rand.nextInt(posts.size()));
            User user = users.get(rand.nextInt(users.size()));
            if (post != null && user != null) {
                CommentPost comment = new CommentPost();
                comment.setComment("Comment on post " + i);
                comment.setCreatedAt(new Date());
                comment.setOwner(user);
                comment.setPost(post);
                comment.setRating(rand.nextInt(5));
                commentPosts.add(comment);
            }
        }
        commentPostRepository.saveAll(commentPosts);
        commentPosts = commentPostRepository.findAll();


        List<Document> documentForSubjects = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = userRepository.findById((long) (i % 20 + 1)).orElse(null);
            if (user != null) {
                Document document = new Document();
                document.setContent("Content of document " + i);
                document.setContentEn("Content of document " + i + " in English");
                document.setType(DocumentType.SUBJECT_DOCUMENT);
                document.setPath(UUID.randomUUID().toString() + "." + docTypes[i % 3]);
                document.setCreatedAt(new Date());
                document.setName("Document " + i + "." + docTypes[i % 5]);
                document.setContentType("application/" + docTypes[i % 5]);
                documentForSubjects.add(document);
            }
        }
        documentRepository.saveAll(documentForSubjects);
        documentForSubjects = documentRepository.findAll();

        // Import 40 SubjectDocuments
        List<SubjectDocument> subjectDocuments = new ArrayList<>();
        String[] semesters = {"20181", "20182", "20191", "20192", "20201", "20202", "20211", "20212"};
        SubjectDocumentType[] types = {SubjectDocumentType.SLIDE, SubjectDocumentType.EXAM, SubjectDocumentType.QUIZ,
                SubjectDocumentType.MIDTERM_EXAM, SubjectDocumentType.FINAL_EXAM, SubjectDocumentType.PROJECT,
                SubjectDocumentType.LECTURE, SubjectDocumentType.HOMEWORK, SubjectDocumentType.TEXTBOOK,
                SubjectDocumentType.REFERENCE_BOOK, SubjectDocumentType.SYLLABUS, SubjectDocumentType.PAPER,
                SubjectDocumentType.HANDOUT};
        for (int i = 0; i < 40; i++) {
            User owner = users.get(rand.nextInt(users.size()));
            Subject subject = subjects.get(rand.nextInt(subjects.size()));
            String semester = semesters[rand.nextInt(semesters.length)];
            SubjectDocumentType type = types[rand.nextInt(types.length)];
            Document document = documentForSubjects.get(rand.nextInt(documentForSubjects.size()));

            SubjectDocument subjectDocument = new SubjectDocument();
            subjectDocument.setType(type);
            subjectDocument.setDocument(document);
            subjectDocument.setDescription("Description " + (i + 1));
            subjectDocument.setDescriptionEn("Description " + (i + 1) + " (English)");
            subjectDocument.setOwner(owner);
            subjectDocument.setSubject(subject);
            subjectDocument.setPublic(rand.nextBoolean());
            subjectDocument.setSemester(semester);

            subjectDocuments.add(subjectDocument);
        }
        subjectDocumentRepository.saveAll(subjectDocuments);
        subjectDocuments = subjectDocumentRepository.findAll();

        // Imports comment for reviewSubject
        List<CommentReviewSubject> commentReviewSubjects = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            ReviewSubject reviewSubject = reviewSubjects.get(rand.nextInt(reviewSubjects.size()));
            User user = users.get(rand.nextInt(users.size()));
            if (reviewSubject != null && user != null) {
                CommentReviewSubject comment = new CommentReviewSubject();
                comment.setComment("Comment on review subject " + i);
                comment.setCreatedAt(new Date());
                comment.setOwner(user);
                comment.setReviewSubject(reviewSubject);
                comment.setRating(rand.nextInt(5));
                commentReviewSubjects.add(comment);
            }
        }
        commentReviewSubjectRepository.saveAll(commentReviewSubjects);
        commentReviewSubjects = commentReviewSubjectRepository.findAll();

        // Imports comment for reviewTeacher
        List<CommentReviewTeacher> commentReviewTeachers = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            ReviewTeacher reviewTeacher = reviewTeachers.get(rand.nextInt(reviewTeachers.size()));
            User user = users.get(rand.nextInt(users.size()));
            if (reviewTeacher != null && user != null) {
                CommentReviewTeacher comment = new CommentReviewTeacher();
                comment.setComment("Comment on review teacher " + i);
                comment.setCreatedAt(new Date());
                comment.setOwner(user);
                comment.setReviewTeacher(reviewTeacher);
                comment.setRating(rand.nextInt(5));
                commentReviewTeachers.add(comment);
            }
        }
        commentReviewTeacherRepository.saveAll(commentReviewTeachers);
        commentReviewTeachers = commentReviewTeacherRepository.findAll();

        // Imports 40 favorite subject
        List<FavoriteSubject> favoriteSubjects = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = users.get(i);
            int index = rand.nextInt(subjects.size() - 10);
            for (int j = 0; j < rand.nextInt(5) + 5; j++) {
                Subject subject = subjects.get(index + j);
                FavoriteSubject favorite = new FavoriteSubject();
                favorite.setUser(user);
                favorite.setSubject(subject);
                favorite.setNotificationType(NotificationType.ALL);
                favoriteSubjects.add(favorite);
            }
        }
        favoriteSubjectRepository.saveAll(favoriteSubjects);
        favoriteSubjects = favoriteSubjectRepository.findAll();


        List<Document> documentForAnswerPost = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User user = userRepository.findById((long) (i % 20 + 1)).orElse(null);
            if (user != null) {
                Document document = new Document();
                document.setContent("Content of document " + i);
                document.setContentEn("Content of document " + i + " in English");
                document.setType(DocumentType.ANSWER_POST);
                document.setPath(UUID.randomUUID().toString() + "." + docTypes[i % 3]);
                document.setCreatedAt(new Date());
                document.setName("Document " + i + "." + docTypes[i % 5]);
                document.setContentType("application/" + docTypes[i % 5]);
                documentForSubjects.add(document);
            }
        }
        documentRepository.saveAll(documentForAnswerPost);
        documentForAnswerPost = documentRepository.findAll();

        // Imports 50 Answer Post
        List<AnswerPost> answerPosts = new ArrayList<>();
        int indexDoc = 0;
        for (int i = 0; i < 10; i++) {
            Post post = posts.get(i);
            for (int j = 0; j < rand.nextInt(3) + 7; j++) {
                AnswerPost answerPost = new AnswerPost();
                answerPost.setContent(j >= 5 ? "Answer " + j + " for post" + i : null);
                answerPost.setType(j >= 5 ? "TEXT" : "DOCUMENT");
                answerPost.setDocument(j < 5 && indexDoc < documentForAnswerPost.size() ? documentForAnswerPost.get(indexDoc++) : null);
                answerPost.setName("Answer " + i);
                answerPost.setPost(post);
                answerPost.setOwner(users.get(rand.nextInt(users.size())));
                answerPosts.add(answerPost);
            }
        }
        answerPostRepository.saveAll(answerPosts);
    }
}