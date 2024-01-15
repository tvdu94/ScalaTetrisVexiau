import HelloIndigo.blocSize
import indigo.{scenes, *}
import indigo.scenes.*
import indigo.shared.datatypes.Fill.Color
import indigo.shared.datatypes.Point
import indigo.shared.scenegraph.RenderNode

import java.awt.Dimension
import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object HelloIndigo extends IndigoSandbox[Unit, Grid] {
  private val magnification = 3

  val config: GameConfig = defaultGameConfig


  val animations: Set[Animation] =
    Set()

  private val yellowTokenName = AssetName("yellowToken")
  private val redTokenName = AssetName("yellowToken")
  val blocSize = 32

  val assets: Set[AssetType] =
    Set(
      AssetType.Image(yellowTokenName, AssetPath("assets/yellowToken.png")),
      AssetType.Image(redTokenName, AssetPath("assets/redToken.png"))
    )

  val fonts: Set[FontInfo] =
    Set()

  val shaders: Set[Shader] =
    Set()


  def setup (assetCollection : AssetCollection,dice : Dice) : Outcome[Startup[Unit]] =

      Outcome(
        Startup.Success(())
      )

  def initialModel(startupData: Unit): Outcome[Grid] = Outcome(new Grid(1024, 720, Batch.empty))

  def updateModel(context : FrameContext[Unit] , g : Grid) : GlobalEvent => Outcome[Grid] = event => Outcome(g)

  def present (context : FrameContext[Unit],g: Grid) : Outcome[SceneUpdateFragment] =



      Outcome(


          SceneUpdateFragment.empty.addLayers(new Grid(1024,1024, Batch.empty).display())

      )
}

case class Grid(xSize:Int,ySize:Int, pieces: Batch[Piece]) {



  def addPiece(piece: Piece): Grid =
    this.copy(pieces = piece :: pieces)

  def display() : Batch[Layer] = {
    var layers = Batch.empty[Layer]
    val lineThickness = 1
    val gridColor = RGBA.Blue

    //vertical lines
    for (x <- 0 until 321 by blocSize) {
      layers = layers.::(Layer(Shape.Line(
        Point(x, 0),
        Point(x, 640),
        Stroke(lineThickness, gridColor))))
    }

    // Draw horizontal lines
    // Skip horizontal lines by offsetting the start point
    for (y <- 0 until 641 by blocSize) {
      layers = layers.::(Layer(Shape.Line(
        Point(0, y),
        Point(320, y),
        Stroke(lineThickness, gridColor))

      ))

    }

    layers
  }

}


object Grid {

}
case class Piece(xPos : Int, yPos : Int, typeToken : PieceColor) {

}

enum PieceColor(assetName: AssetPath){
  case yellowToken extends PieceColor(AssetPath("assets/yellowToken.png"));
  case redToken extends PieceColor(AssetPath("assets/redToken.png"));

}