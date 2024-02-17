import TetrisMain.blocSize
import Shapes.{I, J, L, S}
import indigo.{Fill, Layer, RGBA, Rectangle, Shape, Size, Stroke}
import indigo.shared.collections.Batch
import indigo.shared.datatypes.Point

final case class Grid(xSize: Int, ySize: Int) {

  private var currentPieceX = 4
  private var currentPieceY = 0
  private var pieces = Batch.fill(10)(Batch.fill(20)(0));
  private var currentPiece: Shapes = newPiece()


  /*Affiche l'integralité du jeu (grille, cases, score)*/
  def display(): Batch[Layer] = {
    var layers = Batch.empty[Layer]
    val lineThickness = 1
    val gridColor = RGBA.Blue


    // Draw vertical lines
    for (x <- 0 until 321 by blocSize) {
      layers = layers.::(Layer(Shape.Line(
        Point(x, blocSize),
        Point(x, 640),
        Stroke(lineThickness, gridColor))))
    }

    // Draw horizontal lines
    for (y <- blocSize until 641 by blocSize) {
      layers = layers.::(Layer(Shape.Line(
        Point(0, y),
        Point(320, y),
        Stroke(lineThickness, gridColor))
      ))
    }

    // Draw each bloc in grid
    for (piece <- pieces) {
      layers = layers.++(fillGrid(pieces))
    }
    layers
  }


  /*methode pour remplir la grille et dessiner chaque case en fonction de la piece qui s'y situe*/
  private def fillGrid(pieces: Batch[Batch[Int]]): Batch[Layer] = {

    var layers = Batch.empty[Layer]
    for (x <- 0 until pieces.size by 1) {
      for (y <- 1 until pieces(x).size by 1) {
        pieces(x)(y) match
          case 0 => layers = layers.::(drawCase(x, y, RGBA.Black))
          case 1 => layers = layers.::(drawCase(x, y, RGBA.Orange)) // L
          case 2 => layers = layers.::(drawCase(x, y, RGBA.Cyan)) // I
          case 3 => layers = layers.::(drawCase(x, y, RGBA.DarkBlue)) // J
          case 4 => layers = layers.::(drawCase(x, y, RGBA.Yellow)) // O
          case 5 => layers = layers.::(drawCase(x, y, RGBA.Green)) // S
          case 6 => layers = layers.::(drawCase(x, y, RGBA.Purple)) // T
          case 7 => layers = layers.::(drawCase(x, y, RGBA.Red)) // Z
      }
    }

    layers

  }

  /*dessine une des case de la grid, avec une couleur qui correspond au type de la piece*/
  private def drawCase(x: Int, y: Int, color: RGBA): Layer = {
    Layer(Shape.Box(Rectangle(Point(x * (blocSize) + 2, y * (blocSize) + 1), Size(blocSize - 2, blocSize - 2)), Fill.Color(color)))
  }

  /*recupere une piece aléatoire*/
  def newPiece(): Shapes = {
    // get a random Piece
    Shapes.getRandom
  }

  /*partie appelée toute les 0.8 secondes (normalement, mais dans les faits, il y a un écart de temps)*/
  def playClassic(): Grid = {
    if (canFall(currentPieceX, currentPieceY, currentPiece)) {
      pieces = movePieceBottom()
    }
    else {
      println("cant fall " + this)

      currentPieceY = 0;
      currentPieceX = 4;
      currentPiece = newPiece()
      if (!canFall(currentPieceX, currentPieceY, currentPiece)) {
        return null
      }
    }
    this
  }



  private def canFall(xPos: Int, yPos: Int, shape: Shapes): Boolean = {
    if (yPos == 19) return false

    shape match
      case Shapes.I => pieces(xPos)(yPos + 1) == 0
      case Shapes.J | Shapes.L | Shapes.O => pieces(xPos)(yPos + 1) == 0 && pieces(xPos + 1)(yPos + 1) == 0
      case Shapes.Z => pieces(xPos)(yPos + 1) == 0 && pieces(xPos + 1)(yPos + 1) == 0 && pieces(xPos - 1)(yPos) == 0
      case Shapes.S => pieces(xPos)(yPos + 1) == 0 && pieces(xPos + 1)(yPos + 1) == 0 && pieces(xPos + 2)(yPos) == 0
      case Shapes.T =>
        if (yPos == 18) return false
        pieces(xPos)(yPos + 1) == 0 && pieces(xPos + 1)(yPos + 2) == 0 && pieces(xPos + 2)(yPos + 1) == 0
  }

  /*Cette methode mets à jour toutes les positions dans la grille d'un piece
  lorqu'elle tombe.
  * Cette methode n'est appelée qu'après avoir verifié que le déplacement était possible*/
  private def movePieceBottom(): Batch[Batch[Int]] = {

    currentPiece match
      case Shapes.I =>

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(Math.max(currentPieceY - 3, 0), 0))

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 2))

      case Shapes.J =>
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(Math.max(currentPieceY - 2, 0), 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 3))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 3))

      case Shapes.L =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(Math.max(currentPieceY - 2, 0), 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 1))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 1))


      case Shapes.O =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(Math.max(currentPieceY - 1, 0), 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(Math.max(currentPieceY - 1, 0), 0))

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 4))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 4))

      case Shapes.Z =>
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(Math.max(currentPieceY - 1, 0), 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(Math.max(currentPieceY - 1, 0), 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 7))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 7))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 7))

      case Shapes.S =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(Math.max(currentPieceY - 1, 0), 0))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(Math.max(currentPieceY - 1, 0), 0))

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 5))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 5))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 5))

      case Shapes.T =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 6))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 2, 6))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY + 1, 6))

    currentPieceY = currentPieceY + 1

    pieces
  }

  /*partie appelée lorsque la touche RIGHT_ARROW est appuyée*/
  def playGoRight(): Grid = {
    if (canGoRight(currentPieceX, currentPieceY, currentPiece)) {
      pieces = movePieceRight()
    }
    else {
      println("cant go Right " + this)
    }
    this
  }

  def canGoRight(xPos: Int, yPos: Int, shape: Shapes): Boolean = {
    if (xPos == 9 || yPos < 3) return false
    shape match
      case Shapes.I =>
        pieces(xPos + 1)(yPos) == 0
          && pieces(xPos + 1)(yPos - 1) == 0
          && pieces(xPos + 1)(yPos - 2) == 0
          && pieces(xPos + 1)(yPos - 3) == 0

      case J =>
        if (xPos == 8 || yPos < 2) return false
        pieces(xPos + 2)(yPos) == 0
          && pieces(xPos + 2)(yPos - 1) == 0
          && pieces(xPos + 2)(yPos - 2) == 0

      case L =>
        if (xPos == 8 || yPos < 2) return false
        pieces(xPos + 2)(yPos) == 0
          && pieces(xPos + 1)(yPos - 1) == 0
          && pieces(xPos + 1)(yPos - 2) == 0

      case Shapes.O =>
        if (xPos == 8 || yPos < 1) return false

        pieces(xPos + 2)(yPos) == 0
          && pieces(xPos + 2)(yPos - 1) == 0

      case Shapes.Z =>
        if (xPos == 8 || yPos < 1) return false
        pieces(xPos + 2)(yPos) == 0
          && pieces(xPos + 1)(yPos - 1) == 0
      case Shapes.S =>
        if (xPos == 7 || yPos < 1) return false
        pieces(xPos + 2)(yPos) == 0
          && pieces(xPos + 3)(yPos - 1) == 0
      case Shapes.T =>
        if (xPos == 7 || yPos < 1) return false

        pieces(xPos + 3)(yPos) == 0
          && pieces(xPos + 2)(yPos + 1) == 0
  }

  /*Cette methode mets à jour toutes les positions dans la grille d'un piece lorqu'on la
  * déplace vers la droite.
  * Cette methode n'est appelée qu'après avoir verifié que le déplacement était possible*/
  private def movePieceRight(): Batch[Batch[Int]] = {

    currentPiece match
      case Shapes.I =>

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 2, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 3, 0))

        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 2))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 2))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 2, 2))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 3, 2))

      case Shapes.J =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 2, 0))

        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 3))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY - 1, 3))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY - 2, 3))

      case Shapes.L =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 2, 0))

        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 1))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 1))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 2, 1))


      case Shapes.O =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))

        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 4))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY - 1, 4))

      case Shapes.Z =>
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 7))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 7))

      case Shapes.S =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 0))

        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 5))
        pieces = pieces.update(currentPieceX + 3, pieces(currentPieceX + 3).update(currentPieceY - 1, 5))

      case Shapes.T =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 0))

        pieces = pieces.update(currentPieceX + 3, pieces(currentPieceX + 3).update(currentPieceY, 6))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY + 1, 6))


    currentPieceX = currentPieceX + 1

    pieces
  }

  /*partie appelée lorsque la touche LEFT_ARROW est appuyée*/
  def playGoLeft(): Grid = {
    if (canGoLeft(currentPieceX, currentPieceY, currentPiece)) {
      pieces = movePieceLeft()
    }
    else {
      println("cant go Left " + this)
    }
    this
  }

  def canGoLeft(xPos: Int, yPos: Int, shape: Shapes): Boolean = {
    if (xPos == 0 || yPos < 3) return false
    shape match
      case Shapes.I =>
        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos - 1)(yPos - 1) == 0
          && pieces(xPos - 1)(yPos - 2) == 0
          && pieces(xPos - 1)(yPos - 3) == 0

      case J =>
        if (xPos == 0 || yPos < 2) return false
        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos)(yPos - 1) == 0
          && pieces(xPos)(yPos - 2) == 0

      case L =>
        if (xPos == 0 || yPos < 2) return false
        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos - 1)(yPos - 1) == 0
          && pieces(xPos - 1)(yPos - 2) == 0

      case Shapes.O =>
        if (xPos == 0 || yPos < 1) return false

        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos - 1)(yPos - 1) == 0

      case Shapes.Z =>
        if (xPos == 2 || yPos < 1) return false
        pieces(xPos - 2)(yPos - 2 - 1) == 0
          && pieces(xPos - 2)(yPos) == 0
      case Shapes.S =>
        if (xPos == 0 || yPos < 1) return false
        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos)(yPos - 1) == 0
      case Shapes.T =>
        if (xPos == 0 || yPos < 1) return false

        pieces(xPos - 1)(yPos) == 0
          && pieces(xPos)(yPos + 1) == 0
  }

  /*Cette methode mets à jour toutes les positions dans la grille d'un piece lorqu'on la
  * déplace vers la gauche.
  * Cette methode n'est appelée qu'après avoir verifié que le déplacement était possible*/
  private def movePieceLeft(): Batch[Batch[Int]] = {

    currentPiece match
      case Shapes.I =>

        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 2, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 3, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 2))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 1, 2))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 2, 2))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 3, 2))

      case Shapes.J =>
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 2, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 3))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 3))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 2, 3))

      case Shapes.L =>
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 2, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 1))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 1, 1))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 2, 1))


      case Shapes.O =>
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY - 1, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 4))
        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY - 1, 4))

      case Shapes.Z =>
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 7))
        pieces = pieces.update(currentPieceX - 2, pieces(currentPieceX - 2).update(currentPieceY - 1, 7))

      case Shapes.S =>
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY - 1, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 5))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY - 1, 5))

      case Shapes.T =>
        pieces = pieces.update(currentPieceX + 2, pieces(currentPieceX + 2).update(currentPieceY, 0))
        pieces = pieces.update(currentPieceX + 1, pieces(currentPieceX + 1).update(currentPieceY + 1, 0))

        pieces = pieces.update(currentPieceX - 1, pieces(currentPieceX - 1).update(currentPieceY, 6))
        pieces = pieces.update(currentPieceX, pieces(currentPieceX).update(currentPieceY + 1, 6))


    currentPieceX = currentPieceX - 1

    pieces
  }

  def getPieces(): Batch[Batch[Int]] = {
    pieces
  }
}


object Grid {

}