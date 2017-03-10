package cx.study.auction.http;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.Util;

/**
 * User: LiangLong
 * Date: 2016-10-13
 * Time: 16:01
 * Note: com.microcardio.http
 */

public class FileHttpResponseHandler extends AbstractHttpResponseHandler<File> {

    private static final int BUFFER_SIZE = 1024;

    private final File file;
    private final boolean appened;

    public FileHttpResponseHandler(File file) {
        this(file, false);
    }

    public FileHttpResponseHandler(String path, boolean appened) {
        this(new File(path), appened);
    }

    public FileHttpResponseHandler(@NonNull File file, boolean appened) {
        this.file = file;
        this.appened = appened;
    }

    @Override
    public final File parse(MCHttpResponse body) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException(String.format("can't create file with path %s", file.getAbsolutePath()));
            }
            out = new FileOutputStream(file, appened);
            byte[] buffer = new byte[BUFFER_SIZE];
            int read, delta = 0, hasRead = 0;
            long length = body.contentLength();

            in  = body.contentStream();
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                delta += read; hasRead += read;
                //避免多次刷新UI,限定每获取大于5%的应答数据刷新一次UI
                if (delta * 100 / length > 5) {
                    this.setProgress((int) (hasRead * 100 / length));
                    delta = 0;
                }
            }
            out.flush();
            this.setProgress(100);
            return file;
        }
        finally {
            Util.closeQuietly(in);
            Util.closeQuietly(out);
        }
    }

}
