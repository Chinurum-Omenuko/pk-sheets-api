package com.pk.sheetsAPI.Utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import java.io.ByteArrayInputStream;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleSheetsUtils {

    private static final String APPLICATION_NAME = "PK Sheets API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);

    public static final String DEFAULT_SPREADSHEET_ID = "1zQqfo0UA-69hXZyqSwHnMQyuj0KsvCvlVtrVcRtAlcg";
    public static final String DEFAULT_SHEET_NAME = "Sheet1";

    private final Sheets sheets;

    public GoogleSheetsUtils() throws Exception {
        this.sheets = getSheetService();
    }

    private Sheets getSheetService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(System.getenv("GOOGLE_CREDENTIALS").getBytes()))
                .createScoped(SCOPES);

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME).build();
    }

    public List<List<Object>> readRange(String range) throws IOException {
        String fullRange = DEFAULT_SHEET_NAME + "!" + range;
        ValueRange response = sheets.spreadsheets()
                .values()
                .get(DEFAULT_SPREADSHEET_ID, fullRange)
                .execute();

        return response.getValues();
    }

    public void writeRange(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);

        sheets.spreadsheets().values()
                .append(DEFAULT_SPREADSHEET_ID, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    public void updateRange(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);

        sheets.spreadsheets().values()
                .update(DEFAULT_SPREADSHEET_ID, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

}
