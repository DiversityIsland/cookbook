package ru.cookbook.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.cookbook.dto.image.ImageDto
import ru.cookbook.dto.image.ImageUploadRequest
import ru.cookbook.service.ImageService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestParam file: MultipartFile): UUID {
        val request = ImageUploadRequest(
            data = file.bytes,
            contentType = file.contentType ?: "application/octet-stream"
        )
        return imageService.upload(request)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ImageDto =
        imageService.getById(id)

    @GetMapping("/{id}/data")
    fun getData(@PathVariable id: UUID): ResponseEntity<ByteArray> {
        val image = imageService.getById(id)
        val data = imageService.getData(id)
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(image.contentType))
            .contentLength(image.size)
            .body(data)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        imageService.delete(id)
}
