package com.example.copro.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.exception.BoardNotFoundException;
import com.example.copro.image.api.dto.response.ImageResDto;
import com.example.copro.image.domain.Image;
import com.example.copro.image.domain.repository.ImageRepository;
import com.example.copro.image.exception.ImageNotFoundException;
import com.example.copro.image.exception.NotFoundImageException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageService {
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    @Value("${cloudfront-domain-name}")
    private String CLOUD_FRONT_DOMAIN_NAME; // CloudFront 도메인 이름

    public ImageService(AmazonS3 amazonS3, ImageRepository imageRepository, BoardRepository boardRepository) {
        this.amazonS3 = amazonS3;
        this.imageRepository = imageRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public ImageResDto upload(MultipartFile multipartFile) {
        validateImage(multipartFile.getContentType()); // 이미지 파일인지 확인

 //       containingImageDelete(multipartFile); // 동일한 이름의 이미지가 있으면 삭제
        String fileName = createFileName(multipartFile.getOriginalFilename()); // 파일명 생성

        try (InputStream inputStream = multipartFile.getInputStream()) {
            uploadToBucket(fileName, inputStream, multipartFile.getSize(), multipartFile.getContentType()); // S3 버킷에 업로드

            Image image = createImageEntity(fileName); // Image 엔티티 생성
            imageRepository.save(image); // DB에 저장

            return ImageResDto.of(image); // 결과 반환
        } catch (IOException e) {
            throw new NotFoundImageException(fileName); // 에러 발생 시 예외 던짐
        }
    }

//    @Transactional
//    public void containingImageDelete(MultipartFile multipartFile) {
//        // 동일한 이름의 이미지를 찾아서
//        Image containingImage = imageRepository.findImageByConvertImageNameContaining(multipartFile.getOriginalFilename());
//
//        if (containingImage != null) {
//            delete(containingImage); // 있으면 삭제
//        }
//    }

    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("해당하는 이미지가 없습니다. ID: " + imageId)); // id로 이미지를 찾아서 반환
    }

    @Transactional
    public void delete(Long boardId, Long imageId) {

    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException("해당하는 게시판이 없습니다. ID: " + boardId));

    Image findImage = findById(imageId);

    amazonS3.deleteObject(bucket, findImage.getConvertImageName()); // S3에서 이미지 삭제
        // 게시물과 이미지 사이의 연관 관계 제거
        board.getImages().removeIf(image -> image.getId().equals(imageId));
    }

    public void uploadToBucket(String fileName, InputStream inputStream, long size, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size); // 메타데이터 설정
        objectMetadata.setContentType(contentType);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead); // S3에 업로드할 요청 객체 생성

        amazonS3.putObject(putObjectRequest); // S3에 업로드
    }

    public Image createImageEntity(String fileName) {
        String path = "/" + bucket + "/" + fileName; // S3에서의 경로
        return Image.builder()
                .imageUrl(CLOUD_FRONT_DOMAIN_NAME + path) // 이미지 URL 설정
                .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1)) // 파일명 설정
                .build(); // Image 객체 생성
    }

    public String createFileName(String fileName) {
        return UUID.randomUUID() + "_" + fileName; // 파일명 생성
    }

    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) { // 이미지 파일인지 확인
            throw new IllegalArgumentException(); // 아니면 예외 던짐
        }
    }

    @Transactional // 여러장 업로드
    public List<ImageResDto> uploadMultiple(MultipartFile[] files) {
        List<ImageResDto> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            validateImage(file.getContentType()); // 이미지 파일인지 확인

 //           containingImageDelete(file); // 동일한 이름의 이미지가 있으면 삭제
            String fileName = createFileName(file.getOriginalFilename()); // 파일명 생성

            try (InputStream inputStream = file.getInputStream()) {
                uploadToBucket(fileName, inputStream, file.getSize(), file.getContentType()); // S3 버킷에 업로드

                Image image = createImageEntity(fileName); // Image 엔티티 생성, board 필드는 null로 설정
                imageRepository.save(image); // DB에 저장

                responseList.add(ImageResDto.of(image)); // 결과를 list에 추가
            } catch (IOException e) {
                throw new NotFoundImageException(fileName); // 에러 발생 시 예외 던짐
            }
        }
        return responseList; // 업로드한 모든 이미지에 대한 결과 반환
    }

}