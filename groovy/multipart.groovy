import org.apache.http.HttpEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody

import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA


HttpEntity entity = MultipartEntityBuilder.create()
    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
    .addPart('myfile.txt', new FileBody(new File(getClass().getResource('/myfile.txt').getFile())))
    .build()

def client = new HTTPBuilder('http://example.com')
client.auth.basic 'user', 'password'

client.request(POST, TEXT) { req ->
    uri.path = '/upload'
    requestContentType = MULTIPART_FORM_DATA

    req.entity = entity

    // response handler for a success response code:
    response.success = { resp, text ->
        assert resp.status == 202
    }
}   