package org.swasth;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.net.URL;

public class AwsClient implements ICloudService {

    private final String awsAccesskey;
    private final String awsSecretKey;
    private final String bucketName;

    public AwsClient(String awsAccesskey, String awsSecretKey, String bucketName){
        this.awsAccesskey = awsAccesskey;
        this.awsSecretKey = awsSecretKey;
        this.bucketName = bucketName;
        AmazonS3 s3Client = getClient();
    }

    public AmazonS3 getClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccesskey, awsSecretKey)))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }
    public void putObject(String folderName){
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

    }
    public void putObject(String bucketName,String folderName,String content){
        getClient().putObject(bucketName,folderName,content);
    }

    public void deleteMultipleObject(String folderName){
        String[] objkeyArr = {
                folderName + "/signing_cert_path.pem",
                folderName + "/encryption_cert_path.pem"
        };
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                .withKeys(objkeyArr);
        getClient().deleteObjects(delObjReq);
    }
    public URL getUrl(String bucketName, String path){
       return getClient().getUrl(bucketName,path);
    }
}
