package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class AppController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def app() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.app())
  }
}
