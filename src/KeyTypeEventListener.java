import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class KeyTypeEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    double scale = 1.0;
    double x = 0;
    double y = 0;

    List<ShapeTemplate> arrayList = new ArrayList<>();
    private double xm = 0;
    private double ym = 0;

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(229 / 255f, 229 / 255f, 229 / 255f, 1.0f);
        gl.glViewport(0, 0, 500, 300);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 500, 0, 300, -1, 1);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(scale, scale, 1);
        drawShapes(gl);
        gl.glPopMatrix();
    }

    private void drawShapes(GL gl) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).draw(gl);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int button = e.getKeyCode();
        double convertedMouseX = convertX(xm, e.getComponent().getWidth());
        double convertedMouseY = convertY(ym, e.getComponent().getHeight());
        if (button == KeyEvent.VK_H) {
            arrayList.add(new ShapeTemplate(convertedMouseX, convertedMouseY, ShapeType.HOUSE));
        } else if (button == KeyEvent.VK_F) {
            arrayList.add(new ShapeTemplate(convertedMouseX, convertedMouseY, ShapeType.FLOWER));
        } else if (button == KeyEvent.VK_T) {
            arrayList.add(new ShapeTemplate(convertedMouseX, convertedMouseY, ShapeType.TRIANGLE));
        } else if (button == KeyEvent.VK_S) {
            arrayList.add(new ShapeTemplate(convertedMouseX, convertedMouseY, ShapeType.STAR));
        } else if (button == KeyEvent.VK_C) {
            arrayList.clear();
        } else if (button == KeyEvent.VK_UP) {
            y += 5;
        } else if (button == KeyEvent.VK_DOWN) {
            y -= 5;
        } else if (button == KeyEvent.VK_LEFT) {
            x -= 5;
        } else if (button == KeyEvent.VK_RIGHT) {
            x += 5;
        }

        if (e.isControlDown()) {
            if (button == KeyEvent.VK_EQUALS || button == KeyEvent.VK_ADD) {
                scale *= 1.25; // zoom in
            } else if (button == KeyEvent.VK_MINUS) {
                scale *= 0.25; // zoom out
            }
            if (scale >=10) scale = 10 ; // limit zoom in
            if (scale <=0.1) scale = 0.1 ; // limit zoom out
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        x += e.getX() - xm;
        y -= e.getY() - ym;
        xm = e.getX();
        ym = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xm = e.getX();
        ym = e.getY();
    }

    private double convertX(double x1, double width) {
        return ((x1 / width) * 500 - x) / scale;
    }

    private double convertY(double y1, double height) {
        return ((1 - y1 / height) * 300 - y) / scale;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      int distance = e.getWheelRotation();
      if (distance > 0) {
          scale *= 0.25;
      } else {
          scale *= 1.25;
      }
      if (scale >=10) scale = 10 ; // limit zoom in
      if (scale <=0.1) scale = 0.1 ; // limit zoom out
    }

    enum ShapeType {
        HOUSE, FLOWER, TRIANGLE, STAR
    }

    class ShapeTemplate {
        double centerX, centerY;
        ShapeType shapeType;

        public ShapeTemplate(double centerX, double centerY, ShapeType shapeType) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.shapeType = shapeType;
        }

        public void draw(GL gl) {
            if (shapeType == ShapeType.HOUSE) {
                drawHouse(gl);
            } else if (shapeType == ShapeType.FLOWER) {
                drawFlower(gl);
            } else if (shapeType == ShapeType.TRIANGLE) {
                drawTriangle(gl);
            } else if (shapeType == ShapeType.STAR) {
                drawStar(gl);
            }
        }

        private void drawHouse(GL gl) {
            gl.glColor3f(204f / 255f, 101f / 255f, 25f / 255f);
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glVertex2d(centerX + 30, centerY);
            gl.glVertex2d(centerX - 30, centerY);
            gl.glVertex2d(centerX, centerY + 30);
            gl.glEnd();

            gl.glBegin(GL.GL_POLYGON);
            gl.glVertex2d(centerX + 25, centerY);
            gl.glVertex2d(centerX + 25, centerY - 23);
            gl.glVertex2d(centerX - 25, centerY - 23);
            gl.glVertex2d(centerX - 25, centerY);
            gl.glEnd();
        }

        private void drawFlower(GL gl) {
            double radius = 20.0;
            drawCircle(255f, 185f, 198f, radius, centerX - 3, centerY, gl);
            drawCircle(255f, 185f, 198f, radius, centerX + 7, centerY - 7, gl);
            drawCircle(255f, 185f, 198f, radius, centerX + 7, centerY + 7, gl);
            drawCircle(255f, 185f, 198f, radius, centerX + 15, centerY, gl);
            drawCircle(255f, 249f, 18f, 10.0, centerX + 3, centerY, gl);
        }

        private void drawTriangle(GL gl) {
            gl.glColor3f(29 / 255f, 150 / 255f, 24 / 255f);
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glVertex2d(centerX + 30, centerY);
            gl.glVertex2d(centerX - 30, centerY);
            gl.glVertex2d(centerX, centerY + 50);
            gl.glEnd();
        }

        private void drawStar(GL gl) {
            gl.glColor3f(213 / 255f, 170 / 255f, 3 / 255f);
            gl.glBegin(GL.GL_POLYGON);
            gl.glVertex2d(centerX, centerY);
            gl.glVertex2d(centerX, centerY + 6);
            gl.glVertex2d(centerX + 6, centerY);
            gl.glVertex2d(centerX, centerY - 12);
            gl.glVertex2d(centerX - 4, centerY - 8);
            gl.glVertex2d(centerX - 8, centerY - 10);
            gl.glVertex2d(centerX - 6, centerY);
            gl.glVertex2d(centerX - 8, centerY + 4);
            gl.glVertex2d(centerX - 4, centerY + 2);
            gl.glVertex2d(centerX, centerY + 6);
            gl.glEnd();
        }
    }

    public void drawCircle(float red, float green, float blue, double radius, double centerX, double centerY, GL gl) {
        gl.glColor3f(red / 255f, green / 255f, blue / 255f);
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < 100; i++) {
            double theta = 2.0 * Math.PI * i / 100;
            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);
            gl.glVertex2d(x + centerX, y + centerY);
        }
        gl.glEnd();
    }
}
