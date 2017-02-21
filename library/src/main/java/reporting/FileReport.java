package reporting;

import org.apache.commons.net.ftp.*;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class FileReport implements Report {
    private File tempFile;
    protected FileOutputStream localOutputStream;
    private boolean isLoaded = false;
    private String filename;
    private String name;
    private String text;
    private static final String FTP_SERVER = "ftp.langrsoft.com"; // = "gatekeeper.dec.com";

    public FileReport(String filename) {
        this.filename = filename;

        // copy filename from remote server
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        boolean error = false;
        try {
            int reply;
            ftp.connect(FTP_SERVER);
            reply = ftp.getReplyCode();
            System.out.println("reply code:" + reply);

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new RuntimeException("FTP server refused connection");
            }

            ftp.login("ftp", "");

            // transfer files
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            InputStream inputStream = ftp.retrieveFileStream("robots.txt");

            localOutputStream = new FileOutputStream("local.txt");
            IOUtils.copy(inputStream, localOutputStream);
            localOutputStream.flush();
            IOUtils.closeQuietly(localOutputStream);
            IOUtils.closeQuietly(inputStream);

            ftp.logout();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getText() {
        if (!isLoaded) {
            String[] list = load(filename);
            name = list[0];
            text = list[1];
            isLoaded = true;
        }
        return text;
    }

    @Override
    public String getName() {
        if (!isLoaded) {
            String[] list = load(filename);
            name = list[0];
            text = list[1];
            isLoaded = true;
        }
        return name;
    }

    private String[] load(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String first = reader.readLine();
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String rest = buffer.toString();

            return new String[]{first, rest};

        } catch (IOException e) {
            throw new RuntimeException("unable to load " + filename, e);
        }
    }
}
