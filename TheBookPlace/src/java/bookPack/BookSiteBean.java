package bookPack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

@ManagedBean(name = "bookSiteBean")
@SessionScoped

public class BookSiteBean {

    private String name;
    private String surname;
    private String username;
    private String password;
    private String review;
    private String title;
    private String author;
    private String plot;
    private String genre;
    private String deletedReview;
    private String updatedReview;
    private String currentReview;
    private String cover;
    private String displayedBook;
    private String displayedProfile;
    private int rating;
    private double avgRating;
    private String warning;
    private String searchedBook;
    private String avatar;
    public Connection connection;
    public DataSource dataSource;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSearchedBook() {
        return searchedBook;
    }

    public void setSearchedBook(String searchedBook) {
        this.searchedBook = searchedBook;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getDisplayedProfile() {
        return displayedProfile;
    }

    public void setDisplayedProfile(String displayedProfile) {
        this.displayedProfile = displayedProfile;
    }

    public String getDisplayedBook() {
        return displayedBook;
    }

    public void setDisplayedBook(String displayedBook) {
        this.displayedBook = displayedBook;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCurrentReview() {
        return currentReview;
    }

    public void setCurrentReview(String currentReview) {
        this.currentReview = currentReview;
    }

    public String getUpdatedReview() {
        return updatedReview;
    }

    public void setUpdatedReview(String updatedReview) {
        this.updatedReview = updatedReview;
    }

    public String getDeletedReview() {
        return deletedReview;
    }

    public void setDeletedReview(String deletedReview) {
        this.deletedReview = deletedReview;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BookSiteBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/bookplace");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Connection con() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }
        return connection;
    }

    public String signUp() throws SQLException {  //kaydol

        connection = con();

        String sql = "INSERT INTO users (name,surname,username,password,avatar) VALUES ( ?, ?, ?,? ,?)";

        String sqlc = "select * from users where username='" + getUsername() + "'";
        Statement control = connection.createStatement();
        ResultSet RScontrol = control.executeQuery(sqlc);
        if (RScontrol.next()) {

            warning = "Username already exists.";
            return "indexwarning.xhtml";

        }

        PreparedStatement addEntry = connection.prepareStatement(sql);

        addEntry.setString(1, getName());
        addEntry.setString(2, getSurname());
        addEntry.setString(3, getUsername());
        addEntry.setString(4, getPassword());
        addEntry.setString(5, getAvatar());

        addEntry.executeUpdate();
        return "homepage.xhtml";

    }

    public String logIn() throws SQLException { // girisYap

        connection = con();

        Statement logIn = connection.createStatement();
        String sql = "select * from users where username='" + getUsername() + "'and password='" + getPassword() + "'";
        ResultSet RSlogin = logIn.executeQuery(sql);

        if (RSlogin.next()) {
            return "homepage.xhtml";
        } else {
            warning = "Username or password is incorrect.";
            connection.close();
            return "indexwarning.xhtml";
        }

    }

    public ResultSet getUserReview() throws SQLException { // getProfilYorumlar

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM reviews where username='" + getUsername() + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }

    }

    public ResultSet getProfile() throws SQLException { // getProfil

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM users where username='" + getUsername() + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }

    }

    public ResultSet getOtherUserReview() throws SQLException { // getYabanciProfilYorumlar

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM revıews where username='" + getDisplayedProfile() + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }

    }

    public ResultSet getOtherUser() throws SQLException { // getOtherUser

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM users where username='" + getDisplayedProfile() + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();
        }

    }

    public String deleteReview() throws SQLException { // deleteReview

        connection = con();
        PreparedStatement ps = connection.prepareStatement("delete from reviews where review=?");
        ps.setString(1, getDeletedReview());
        ps.executeUpdate();
        return "profile.xhtml";
    }

    public String updateReview() throws SQLException { // updateReview

        connection = con();
        PreparedStatement ps = connection.prepareStatement("update reviews set review=? where review='" + getUpdatedReview() + "'");
        ps.setString(1, getCurrentReview());
        ps.executeUpdate();

        return "profile.xhtml";
    }

    public ResultSet getBooks() throws SQLException { // getBooks

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM books ORDER BY RANDOM()");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }
    }

    public ResultSet getAllBooks() throws SQLException { // getAllBooks

        connection = con();
        try {
            PreparedStatement object = connection.prepareStatement("SELECT * FROM books");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }
    }

    public String addBook() throws SQLException {

        connection = con();

        String sql = "INSERT INTO books (title,author,genre,summary) VALUES ( ?, ?, ?,? )";

        PreparedStatement addEntry = connection.prepareStatement(sql);

        addEntry.setString(1, getTitle());
        addEntry.setString(2, getAuthor());
        addEntry.setString(3, getGenre());
        addEntry.setString(4, getPlot());

        sql = "select * from books where title='" + getTitle() + "'";
        Statement control = connection.createStatement();
        ResultSet RScontrol = control.executeQuery(sql);
        if (RScontrol.next()) {
            FacesMessage message = new FacesMessage("Book already exists.");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
            return "message";

        } else {
            addEntry.executeUpdate();

            return "homepage.xhtml";
        }

    }


    public double highestRate(int n) throws SQLException {

        connection = con();
        String sql = "SELECT CAST(AVG(rating) AS NUMERIC(2,1)) FROM reviews GROUP BY title ORDER BY AVG(rating) DESC \n" +
"                    OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
        Statement control=connection.createStatement();
        ResultSet rs =control.executeQuery(sql);
        double rate= 0.0;
        if(rs.next()) {
            rate=((Number) rs.getObject(1)).doubleValue();
        }
        return rate;
    }

    public ResultSet highestRatedBook(int n) throws SQLException { // enYuksekPuanAlanKitap

        connection = con();

        try {

            String sql = "SELECT title ,AVG(rating) as rating FROM reviews GROUP BY title ORDER BY rating DESC "
                    + "OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
            Statement control = connection.createStatement();
            ResultSet RScontrol = control.executeQuery(sql);
            String book = "Harry Potter";
            if (RScontrol.next()) {
                book = RScontrol.getString(1);
            }
            PreparedStatement object = connection.prepareStatement("select * from books where title='" + book + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }
    }


        public int reviewCount(int n) throws SQLException {

        connection = con();
        String sql = "SELECT COUNT(review) as rv FROM reviews where review !='' GROUP BY title ORDER BY rv DESC \n" +
"                    OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
        Statement control=connection.createStatement();
        ResultSet rs =control.executeQuery(sql);
        int rate=0;
        if(rs.next()) {
            rate=rs.getInt(1);
        }
        return rate;
    }

    public ResultSet mostReviewedBook(int n) throws SQLException { // enCokYorumAlanKitap

        connection = con();

        try {

            String sql = "select title, COUNT(review) AS MRV from reviews where review !='' GROUP BY title "
                    + "ORDER BY MRV DESC OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
            Statement control = connection.createStatement();
            ResultSet RScontrol = control.executeQuery(sql);
            String book = "Harry Potter";  //burda da Harry Potter'a setlemişler
            if (RScontrol.next()) {
                book = RScontrol.getString(1);
            }
            PreparedStatement object = connection.prepareStatement("select * from books where title='" + book + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();

        }
    }


    public ResultSet mostRatedBook(int n) throws SQLException { // enCokOyAlanKitap

        
        connection = con();

        try {

            String sql = "select title, COUNT(rating) AS MOST_FREQUENT from reviews GROUP BY title "
                    + "ORDER BY COUNT(rating) DESC OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
            Statement control = connection.createStatement();
            ResultSet RScontrol = control.executeQuery(sql);
            String book = "Harry Potter"; // bu da Harry Potter
            if (RScontrol.next()) {
                book = RScontrol.getString(1);
            }
            PreparedStatement object = connection.prepareStatement("select * from books where title='" + book + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();
        }

    }
    
            public int ratingCount(int n) throws SQLException {

        connection = con();
        String sql = "SELECT COUNT(rating) as rt FROM reviews GROUP BY title ORDER BY rt DESC \n" +
"                    OFFSET "+(n-1)+" ROWS FETCH NEXT 1 ROW ONLY";
        Statement control=connection.createStatement();
        ResultSet rs =control.executeQuery(sql);
        int rate=0;
        if(rs.next()) {
            rate=rs.getInt(1);
        }
        return rate;
    }

    public ResultSet displayBook() throws SQLException {  // kitabiGoruntule

        connection = con();
        PreparedStatement object = connection.prepareStatement("select * from books where title='" + getDisplayedBook() + "'");
        CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
        resultSet.populate(object.executeQuery());

        return resultSet;
    }

    public String displayAvgRate() throws SQLException {

        connection = con();
        String sql = "select CAST(AVG(rating) AS NUMERIC(2,1)) from reviews where title='"+getDisplayedBook()+"' group by title";
        Statement control=connection.createStatement();
        ResultSet rs =control.executeQuery(sql);
        String rate="";
        if(rs.next()) {
            rate=rs.getString(1);
        }
        return rate;
    } 
    
    public int displayReviewCount() throws SQLException {

        connection = con();
        String sql = "select COUNT(review) from reviews where review !='' and title='Atomic Habits' ";
        Statement control=connection.createStatement();
        ResultSet rs =control.executeQuery(sql);
        int rate=0;
        if(rs.next()) {
            rate=rs.getInt(1);
        }
        return rate;
    } 
    
    public ResultSet booksReviews() throws SQLException {  // kitabinYorumlari

        connection = con();
        PreparedStatement object = connection.prepareStatement("select * from reviews where title='" + getDisplayedBook() + "'");
        CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
        resultSet.populate(object.executeQuery());

        return resultSet;
    }

    
    //SELECT AVG(rating) as avgr FROM reviews  where title = '" + getTitle() + "' GROUP BY title
   
  /*  public ResultSet avgRating() throws SQLException { // searchBook

        connection = con();

        try {

            PreparedStatement object = connection.prepareStatement("SELECT AVG(rating) FROM reviews  where title = '" + getTitle() + "' GROUP BY title");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();
        }
    } */

    
    
    public String review() throws SQLException {  // yorumEkle

        connection = con();

        String sql = "INSERT INTO reviews (username,title,rating,review) VALUES "
                + "( '" + getUsername() + "','" + getDisplayedBook() + "' ,?,? )";

        PreparedStatement addEntry = connection.prepareStatement(sql);

        if (getRating() < 1 || getRating() > 5) {
            //error message
            return "book.xhtml";
        }

        addEntry.setInt(1, getRating());
        addEntry.setString(2, getReview());

        addEntry.executeUpdate();

        return "book.xhtml";
    }

    public ResultSet searchBook() throws SQLException { // searchBook

        connection = con();

        try {

            PreparedStatement object = connection.prepareStatement("select * from books where title='" + getSearchedBook() + "'");
            CachedRowSet resultSet = new com.sun.rowset.CachedRowSetImpl();
            resultSet.populate(object.executeQuery());

            return resultSet;
        } finally {
            connection.close();
        }
    }

    public String addAvatar() throws SQLException { // avatarEkle

        connection = con();
        String sql = "UPDATE users set avatar='" + getAvatar() + "' where username='" + getUsername() + "'";

        PreparedStatement addEntry = connection.prepareStatement(sql);

        addEntry.executeUpdate();

        return "homepage.xhtml";
    }
}
