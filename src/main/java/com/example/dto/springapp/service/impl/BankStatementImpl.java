package com.example.dto.springapp.service.impl;

import com.example.dto.springapp.dtos.request.EmailDetailsRequest;
import com.example.dto.springapp.dtos.response.TransResponse;
import com.example.dto.springapp.dtos.response.TransactionResponse;
import com.example.dto.springapp.model.Transaction;
import com.example.dto.springapp.model.User;
import com.example.dto.springapp.repository.TransactionRepository;
import com.example.dto.springapp.repository.UserRepository;
import com.example.dto.springapp.service.BankStatement;
import com.example.dto.springapp.service.EmailService;
import com.example.dto.springapp.utils.AccountUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BankStatementImpl implements BankStatement {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.file.pdf.url}")
    private String file;

    public TransactionResponse generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {


        String loggedInUser = AccountUtils.getLoggedInUser();

        User user = userRepository.findByEmail(loggedInUser);

        String accountName = user.getAccountName();
        String accountAddress = user.getAddress();
        String accountEmail = user.getEmail();
        boolean isValid = user.getAccountNumber().equals(accountNumber);

        if (isValid) {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

            List<Transaction> transactionsList = transactionRepository.findAllByAccountNumber(accountNumber)
                    .stream()
                    .filter(transaction -> !transaction.getTimestamp().isBefore(startDateTime)
                            && transaction.getTimestamp().isBefore(endDateTime))
                    .toList();
            List<TransResponse> response = new ArrayList<>();

            log.info("designing statement");
            designStatement(accountName, accountAddress, startDate, endDate, transactionsList, accountEmail);

            for (Transaction transaction : transactionsList) {
                TransResponse res = TransResponse.builder()
                        .transactionType(transaction.getTransactionType())
                        .status(transaction.getStatus())
                        .amount(transaction.getAmount())
                        .timeStamp(transaction.getTimestamp())
                        .transactionId(transaction.getTransactionId())
                        .build();
                response.add(res);
            }

            return TransactionResponse.builder()
                    .responseCode("001")
                    .responseMessage("SUCCESS")
                    .transResponse(response)
                    .build();
        }
        return TransactionResponse.builder()
                .responseCode("000")
                .responseMessage("Invalid account number")
                .transResponse(null)
                .build();

    }


//    private void designStatement(String customerName, String accountAddress, String startDate, String endDate,
//                                 List<Transaction> transactionsList, String accountEmail) throws FileNotFoundException,
//            DocumentException {
//        Rectangle statementSize = new Rectangle(PageSize.A4);
//        Document document = new Document(statementSize);
//        OutputStream outputStream = new FileOutputStream(file);
//
//        PdfWriter.getInstance(document, outputStream);
//        document.open();
//
//        PdfPTable table = new PdfPTable(1);
//        PdfPCell bankName = new PdfPCell(new Phrase("The black Bank"));
//        bankName.setBorder(0);
//        bankName.setBackgroundColor(BaseColor.BLUE);
//        bankName.setPadding(20f);
//
//        PdfPCell bankAddress = new PdfPCell(new Phrase("80 Wall Street Victoria Island"));
//        bankAddress.setBorder(0);
//        table.addCell(bankName);
//        table.addCell(bankAddress);
//
//        PdfPTable statementInfo = new PdfPTable(2);
//        PdfPCell customerInfo = new PdfPCell(new Phrase("Start date: " + startDate));
//        customerInfo.setBorder(0);
//
//        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
//        statement.setBorder(0);
//
//        PdfPCell stopDate = new PdfPCell(new Phrase("Stop date: " + endDate));
//        stopDate.setBorder(0);
//
//        PdfPCell name = new PdfPCell(new Phrase("Customer name: " + customerName));
//        name.setBorder(0);
//
//        PdfPCell space = new PdfPCell();
//        space.setBorder(0);
//
//        PdfPCell address = new PdfPCell(new Phrase("Customer address: " + accountAddress));
//        address.setBorder(0);
//
//        PdfPTable transactionTable = new PdfPTable(4);
//        PdfPCell date = new PdfPCell(new Phrase("DATE"));
//        date.setBorder(0);
//        date.setBackgroundColor(BaseColor.BLUE);
//
//        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
//        transactionType.setBorder(0);
//        transactionType.setBackgroundColor(BaseColor.BLUE);
//
//        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
//        transactionAmount.setBorder(0);
//        transactionAmount.setBackgroundColor(BaseColor.BLUE);
//
//        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
//        status.setBorder(0);
//        status.setBackgroundColor(BaseColor.BLUE);
//
//        transactionTable.addCell(date);
//        transactionTable.addCell(transactionAmount);
//        transactionTable.addCell(transactionType);
//        transactionTable.addCell(status);
//
//        transactionsList.forEach(transaction -> {
//                    transactionTable.addCell(new Phrase(transaction.getTimestamp().toString()));
//                    transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
//                    transactionTable.addCell(new Phrase(transaction.getTransactionType()));
//                    transactionTable.addCell(new Phrase(transaction.getStatus()));
//                }
//        );
//
//        statementInfo.addCell(customerInfo);
//        statementInfo.addCell(statement);
//        statementInfo.addCell(endDate);
//        statementInfo.addCell(customerName);
//        statementInfo.addCell(space);
//
//        log.info("here now");
//
//        document.add(table);
//        document.add(statementInfo);
//        document.add(transactionTable);
//
//        document.close();
//
//
//        EmailDetailsRequest email = EmailDetailsRequest.builder()
//                .subject("bank account statement")
//                .recipient(accountEmail)
//                .messageBody("kindly find your bank statement attached.")
//                .attachment(file)
//                .build();
//          // sends banks statement to email.
//         //  emailService.sendEmailAlertWithAttachment(email);
//    }

    private void designStatement(String customerName, String accountAddress, String startDate, String endDate,
                                 List<Transaction> transactionsList, String accountEmail) throws FileNotFoundException, DocumentException {

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize, 40, 40, 40, 40); // Add margins
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter.getInstance(document, outputStream);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE);
        Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10);

        document.open();

        // Header Table
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell bankNameCell = new PdfPCell(new Phrase("THE BLACK BANK", headerFont));
        bankNameCell.setBorder(Rectangle.NO_BORDER);
        bankNameCell.setBackgroundColor(BaseColor.BLUE);
        bankNameCell.setPadding(20f);
        bankNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell bankAddressCell = new PdfPCell(new Phrase("80 Wall Street, Victoria Island, Lagos", bodyFont));
        bankAddressCell.setBorder(Rectangle.NO_BORDER);
        bankAddressCell.setPaddingBottom(15f);
        bankAddressCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        headerTable.addCell(bankNameCell);
        headerTable.addCell(bankAddressCell);
        document.add(headerTable);

        // Statement Info Table
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(20f);
        infoTable.setSpacingAfter(20f);

        infoTable.addCell(createInfoCell("Customer Name:", customerName, bodyFont));
        infoTable.addCell(createInfoCell("Statement of Account", "", subHeaderFont));
        infoTable.addCell(createInfoCell("Customer Address:", accountAddress, bodyFont));
        infoTable.addCell(createInfoCell("Start Date:", startDate, bodyFont));
        infoTable.addCell(createInfoCell("Email:", accountEmail, bodyFont));
        infoTable.addCell(createInfoCell("End Date:", endDate, bodyFont));

        document.add(infoTable);

        // Transactions Table
        PdfPTable transactionTable = new PdfPTable(4);
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(10f);
        transactionTable.setWidths(new float[]{2f, 3f, 3f, 2f});

        transactionTable.addCell(createHeaderCell("DATE"));
        transactionTable.addCell(createHeaderCell("TRANSACTION TYPE"));
        transactionTable.addCell(createHeaderCell("AMOUNT"));
        transactionTable.addCell(createHeaderCell("STATUS"));

        for (Transaction transaction : transactionsList) {
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getTimestamp().toString(), bodyFont)));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getTransactionType(), bodyFont)));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getAmount().toString(), bodyFont)));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getStatus(), bodyFont)));
        }

        document.add(transactionTable);
        document.close();

        // Email with PDF attachment
        EmailDetailsRequest email = EmailDetailsRequest.builder()
                .subject("Your Account Statement from The Black Bank")
                .recipient(accountEmail)
                .messageBody("Dear " + customerName + ",\n\nPlease find your account statement attached.\n\nBest regards,\nThe Black Bank Team")
                .attachment(file)
                .build();

        // emailService.sendEmailAlertWithAttachment(email);
        log.info("Statement successfully created and ready for email.");
    }

    private PdfPCell createInfoCell(String label, String value, Font font) {
        Phrase phrase = new Phrase(label + " " + value, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10f);
        return cell;
    }

    private PdfPCell createHeaderCell(String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8f);
        return cell;
    }

}
