package hw3;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class ManagerPrint implements Printable {

  @Override
  public int print(Graphics g, PageFormat pF, int pageIndex) throws PrinterException {
    // TODO Auto-generated method stub
    if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
      return NO_SUCH_PAGE;
    }

    /*
     * User (0,0) is typically outside the imageable area, so we must translate
     * by the X and Y values in the PageFormat to avoid clipping
     */
    Graphics2D g2d = (Graphics2D) g;
    g2d.translate(pF.getImageableX(), pF.getImageableY());

    //for (int i = 0; i < g.getClip().getBounds().getWidth(); i++) {
      
    //}
    
    /* Now we perform our rendering */
    g.drawString("\"Meowith\" to trigger Brett", 16, 16);
    g.drawString("p", 16, 32);
    g.drawString("l", 19, 32);
    //g.drawString("o", 19, 32);
    System.out.println(g.getFontMetrics());
    //g.drawRect(100, 98, 160, 100);

    /* tell the caller that this page is part of the printed document */
    return PAGE_EXISTS;
  }

  public static void main(String[] args) {

    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(new ManagerPrint());
    boolean ok = job.printDialog();
    if (ok) {
      try {
        job.print();
      } catch (PrinterException ex) {
        /* The job did not successfully complete */
      }
    }
  }

}
