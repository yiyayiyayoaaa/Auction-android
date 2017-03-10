package cx.study.auction.http;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cx.study.auction.util.IOUtils;

/**
 * User: LiangLong
 * Date: 2016-10-09
 * Time: 23:45
 * Note: com.microcardio.http
 * http同步请求结果转换器接口,自带3个结果转换器,有需要请自行扩展
 */

public interface IParser<Result> {

    Result parse(MCHttpResponse body) throws IOException;

    class BytesParser implements IParser<byte[]> {

        @Override
        public byte[] parse(MCHttpResponse body) throws IOException {
            return IOUtils.stream2Bytes(body.contentStream());
        }

    }

    /**
     * String 转换器
     */
    class StringParser implements IParser<String> {

        @Override
        public String parse(MCHttpResponse body) throws IOException {
            return IOUtils.stream2String(body.contentStream());
        }

    }

    /**
     * JSONObject 转换器
     */
    class JSONObjectParser implements IParser<JSONObject> {

        @Override
        public JSONObject parse(MCHttpResponse body) throws IOException {
            String s = IOUtils.stream2String(body.contentStream());
            try {
                return new JSONObject(s);
            } catch (JSONException e) {
                throw new IOException(String.format("%s is not a JSONObject !", s));
            }
        }

    }

    /**
     * JSONArray 转换器
     */
    class JSONArrayParser implements IParser<JSONArray> {

        @Override
        public JSONArray parse(MCHttpResponse body) throws IOException {
            String s = IOUtils.stream2String(body.contentStream());
            try {
                return new JSONArray(s);
            } catch (JSONException e) {
                throw new IOException(String.format("%s is not a JSONArray !", s));
            }
        }

    }

}
