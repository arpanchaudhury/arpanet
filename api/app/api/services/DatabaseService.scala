package api.services

import java.io.File

import com.google.inject.{Inject, Singleton}

@Singleton
class DatabaseService @Inject()(propertyService: PropertyService) {

  val databases = propertyService.mongoServers

  def getPublicImage(imageId: String) = {
    val imagePath = "resources/test_image.jpg"
    new File(imagePath)
  }

  def getPhotograph(imageId: String) = {
    val imagePath = "resources/test_image.jpg"
    new File(imagePath)
  }
}
