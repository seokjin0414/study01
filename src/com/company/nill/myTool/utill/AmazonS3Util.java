package com.company.nill.myTool.utill;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Util {
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    @Autowired
    private AmazonS3 amazonS3;

    public final String AWS_FILE_URL_KEY = "AWS_FILE_URL_KEY";

    public ListObjectsV2Result read(String key) {
        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(BUCKET_NAME, key);
        return listObjectsV2Result;
    }

    public String uploadS3(MultipartFile file, String key) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, key, byteArrayInputStream, objectMetadata));

        return amazonS3.getUrl(BUCKET_NAME, key).toString();
    }

    /**
     * 파일 이동
     * @param sourceKey
     * @param destinationKey
     * @see "https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/dev/CopyingObjectUsingJava.html"
     */
    public CopyObjectResult copyFile(String sourceKey, String destinationKey) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(BUCKET_NAME, sourceKey, BUCKET_NAME, destinationKey);
        return amazonS3.copyObject(copyObjectRequest);
    }

    public void deleteS3(String key) {
        amazonS3.deleteObject(BUCKET_NAME, key);
    }

    /**
     * Amazon S3 폴더 생성
     * @param key
     */
    public void createFolder(String key) {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, key, input, metadata));
    }


    public ObjectMetadata uploadS3ByBase64(String fileContent, String file_type, String path, HashMap<String, Object> param) {

        ObjectMetadata return_map = null;

        try {

            String file_name = DateUtils.getNowDateString("yyyyMMdd_HHmmss") + "_" + StringUtils.getRandomStr(4) + ".png";

            String file_url = path + "/" + file_name;

            byte[] bytes = fileContent.getBytes();

            if (fileContent.indexOf(",") > -1) {
                fileContent = fileContent.substring(fileContent.indexOf(",") + 1);
            }

            byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64(fileContent.getBytes());

            InputStream fis = new ByteArrayInputStream(bI);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file_type);
            metadata.setContentLength(bI.length);

            //amazonS3.putObject(BUCKET_NAME, file_url, fis, metadata);

            amazonS3.putObject(
                    new PutObjectRequest(BUCKET_NAME, file_url, fis, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return_map = amazonS3.getObjectMetadata(BUCKET_NAME, file_url);

            param.put(AWS_FILE_URL_KEY, amazonS3.getUrl(BUCKET_NAME, file_url).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return return_map;
    }


}
