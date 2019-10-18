package request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InterruptedIOException;

public class HttpGetRequest {
    //httpgetreq
    private String webContext;

    public String getWebContext()
    {
        return this.webContext;
    }

    public void setWebContext(String webContext)
    {
        this.webContext = webContext;
    }

    public int requestHttp(String url)
    {
        int status = 0;
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        HttpGet mPost = new HttpGet(url);

        try
        {
            mHttpClient.getParams().setIntParameter("http.socket.timeout", 3000);
            mHttpClient.getParams().setIntParameter("http.connection.timeout", 3000);
            HttpResponse response = mHttpClient.execute(mPost);
            int res = response.getStatusLine().getStatusCode();
            if (res == 200)
            {
                System.out.println("连接响应值200................");
                HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    String info = EntityUtils.toString(entity);
                    setWebContext(info);
                    status = 1;
                }
            }
            else if (res == 404)
            {
                status = 404;
            }
            else if (res == 500)
            {
                status = 500;
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
            status = 900;
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
            status = 901;
        }
        catch (InterruptedIOException e)
        {
            e.printStackTrace();
            status = 902;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            status = 903;
        }
        return status;
    }
}
