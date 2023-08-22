package vuvanhai568.project.appDocTruyen.Modal;


import java.util.List;

public class Book {
    private String _id;

    private String title;
    private String content;
    private String image;
    private String author;
    private String idauthor;
    private String date;
    private List<Comment> comment;

    public Book() {
    }

    public Book(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public Book(String _id, String title, String content, String image, String author, String idauthor, String date, List<Comment> comment) {
        this._id = _id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.author = author;
        this.idauthor = idauthor;
        this.date = date;
        this.comment = comment;
    }
    public int getBinhLuanCount() {
        if (comment != null) {
            return comment.size();
        } else {
            return 0;
        }
    }
    public static class Comment {
        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        private String idPerson;
        private String fullName;
        private String cmt;

        public Comment(String cmt) {
            this.cmt = cmt;
        }

        public String getIdPerson() {
            return idPerson;
        }

        public void setIdPerson(String idPerson) {
            this.idPerson = idPerson;
        }

        public String getFullName() {
            return fullName;
        }

        public Comment(String _id, String idPerson, String fullName, String cmt) {
            this._id = _id;
            this.idPerson = idPerson;
            this.fullName = fullName;
            this.cmt = cmt;
        }
        public Comment( String idPerson, String fullName, String cmt) {

            this.idPerson = idPerson;
            this.fullName = fullName;
            this.cmt = cmt;
        }
        public Comment() {
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getCmt() {
            return cmt;
        }

        public void setCmt(String cmt) {
            this.cmt = cmt;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Comment> getBinhLuan() {
        return comment;
    }

    public void setBinhLuan(List<Comment> comment) {
        this.comment = comment;
    }

    public Book(String title, String content, String image, String author, String idauthor, String date) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.author = author;
        this.idauthor = idauthor;
        this.date = date;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
