/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.assistant.model.Book;
import library.assistant.model.BookDAO;
import library.assistant.model.IssueDAO;
import library.assistant.model.IssueInfo;
import library.assistant.model.Member;
import library.assistant.model.MemberDAO;

/**
 *
 * @author ASUS
 */
public class MainController implements Initializable {

    @FXML
    private Button newBookBtn;
    @FXML
    private Button newMemberBtn;
    @FXML
    private Button bookListBtn;
    @FXML
    private Button memberListBtn1;
    @FXML
    private JFXTextField bookIDSearchField;
    @FXML
    private Text titleText;
    @FXML
    private Text authorText;
    @FXML
    private JFXTextField memberIDSearchField;
    private BookDAO bookDAO;
    @FXML
    private Text nameText;
    @FXML
    private Text mobileText;
    @FXML
    private Text addressText;
    private MemberDAO memberDAO;
    @FXML
    private JFXButton issueBtn;
    @FXML
    private Text availableText;
    private IssueDAO issueDAO;
    @FXML
    private JFXTextField bookIDField;
    @FXML
    private Text mNameText;
    @FXML
    private Text mMobileText;
    @FXML
    private Text mAddressText;
    @FXML
    private Text bTitleText;
    @FXML
    private Text bAuthorText;
    @FXML
    private Text bPublisherText;
    @FXML
    private Text issueDateText;
    @FXML
    private Text renewCountText;
    @FXML
    private JFXButton submissionBtn;
    @FXML
    private JFXButton renewBtn;
    @FXML
    private MenuItem closeBtn;
    @FXML
    private MenuItem dbPreference;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issueDAO = new IssueDAO();
    }

    @FXML
    private void loadNewBookWindow(ActionEvent event) throws IOException {
        loadWindow("New Book", "/library/assistant/view/newbook.fxml");
    }

    @FXML
    private void loadNewMemberWindow(ActionEvent event) throws IOException {
        loadWindow("New Member", "/library/assistant/view/newmember.fxml");
    }

    @FXML
    private void loadBooksListWindow(ActionEvent event) throws IOException {
        loadWindow("Book List", "/library/assistant/view/bookslist.fxml");
    }

    @FXML
    private void loadMembersListWindow(ActionEvent event) throws IOException {
        loadWindow("Member List", "/library/assistant/view/memberslist.fxml");
    }

    public void loadWindow(String title, String url) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(newBookBtn.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.show();
    }

    @FXML
    private void searchBookInfo(ActionEvent event) {

        clearBookCache();
        String bookID = bookIDSearchField.getText();

        if (bookID.isEmpty()) {
            System.out.println("Please enter book id first.");
        }

        try {
            Book book = bookDAO.getBook(bookID);
            if (book != null) {
                titleText.setText(book.getTitle());
                authorText.setText(book.getAuthor());
                String availableStr = book.isAvailable() ? "Available" : "Not Available";
                availableText.setText(availableStr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearBookCache() {
        titleText.setText("-");
        authorText.setText("-");
        availableText.setText("-");
    }

    @FXML
    private void searchMemberInfo(ActionEvent event) {

        clearMemberCache();
        String memberID = memberIDSearchField.getText();

        if (memberID.isEmpty()) {
            System.out.println("Please enter member id first.");
        }
        try {
            Member member = memberDAO.getMember(memberID);
            if (member != null) {
                nameText.setText(member.getName());
                mobileText.setText(member.getMobile());
                addressText.setText(member.getAddress());
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearMemberCache() {
        nameText.setText("-");
        mobileText.setText("-");
        addressText.setText("-");
    }

    @FXML
    private void issueBook(ActionEvent event) {
        String memberId = memberIDSearchField.getText();
        String bookId = bookIDSearchField.getText();

        if (memberId.isEmpty() || bookId.isEmpty()) {
            System.out.println("Plz enter member id and book id first.");
            return;
        }

        try {
            if (issueDAO.checkBookAvailable(bookId)) {
                System.out.println("This book was already issued.");
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            bookDAO.updateBook(bookId, false);
            issueDAO.saveIssueInfo(memberId, bookId);
            System.out.println("Book was successfully issued.");
        } catch (SQLException ex) {
            System.out.println("Book Issue Operation failed.");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void searchIssuedBookInfo(ActionEvent event) {

        clearCacheIssuedBook();
        String bookId = bookIDField.getText();

        if (bookId.isEmpty()) {
            System.out.println("Please enter book id first.");
        }
        try {
            IssueInfo issueInfo = issueDAO.searchIssuedInfo(bookId);
            if (issueInfo != null) {
                Book book = bookDAO.getBook(issueInfo.getBookId());
                bTitleText.setText(book.getTitle());
                bAuthorText.setText(book.getAuthor());
                bPublisherText.setText(book.getPublisher());
                Member member = memberDAO.getMember(issueInfo.getMemberId());
                mNameText.setText(member.getName());
                mMobileText.setText(member.getMobile());
                mAddressText.setText(member.getAddress());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY, E");
                String dateStr = dateFormat.format(issueInfo.getIssueDate());
                issueDateText.setText(dateStr);
                renewCountText.setText("Renew Count:" + issueInfo.getRenewCount());
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearCacheIssuedBook() {
        mNameText.setText("-");
        mMobileText.setText("-");
        mAddressText.setText("-");
        bTitleText.setText("-");
        bAuthorText.setText("-");
        bPublisherText.setText("-");
        issueDateText.setText("-");
        renewCountText.setText("-");

    }

    @FXML
    private void startSubmissionBook(ActionEvent event) {

        String bookId = bookIDField.getText();

        try {
            issueDAO.deleteIssueInfo(bookId);
            bookDAO.updateBook(bookId, true);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void renewBook(ActionEvent event) {

        String bookId = bookIDField.getText();
        // int count;
        if (bookId.isEmpty()) {
            System.out.println("Please enter book id first.");
        }

        try {
//            IssueInfo issueInfo=issueDAO.searchIssuedInfo(bookId);
//            count=issueInfo.getRenewCount();
            issueDAO.updateIssueInfo(bookId);
            System.out.println("Book was successfully renewed.");
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showDatabaseConfigWindow(ActionEvent event) throws IOException {

        loadWindow("Server", "/library/assistant/view/server.fxml");
    }

    @FXML
    private void closeMainWindow(ActionEvent event) throws IOException {

        Stage stage = (Stage) newMemberBtn.getScene().getWindow();
        stage.close();
    }

}
