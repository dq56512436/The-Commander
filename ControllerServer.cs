using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using WindowsFormsApplication1;

namespace WindowsFormsApplication1
{
    public struct INPUT
    {
        public int type;
        public MOUSEINPUT mi;
    }

    public struct MOUSEINPUT
    {
        public int dx;
        public int dy;
        public int mouseData;
        public int dwFlags;
        public int time;
        public int dwExtraInfo;
    }

    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private Socket s;
        private int xresMob;
        private int yresMob;
        private int xresDesk;
        private int yresDesk;
        private int countForClickODrag=0;

        private bool downFlag = false;

        [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention = CallingConvention.Cdecl)]
        public static extern void mouse_event(long dwFlags, long dx, long dy, long cButtons, long dwExtraInfo);

        [DllImport("User32.dll", SetLastError = true)]
        public static extern int SendInput(int nInputs, ref INPUT pInputs, int cbSize);

        private const int MOUSEEVENTF_LEFTDOWN = 0x02;
        private const int MOUSEEVENTF_LEFTUP = 0x04;
        private const int MOUSEEVENTF_RIGHTDOWN = 0x08;
        private const int MOUSEEVENTF_RIGHTUP = 0x10;
        private const int MOUSEEVENTF_WHEEL = 0x0800;
        private const int MOUSEEVENTF_MOVE = 0x0001;
        private const int MOUSEEVENTF_ABSOLUTE = 0x8000;

        public void run()
        {
            int x = 0; int y = 0, k = 0;
            while (true)
            {
                byte[] b = new byte[100];
                try
                {
                    k = s.Receive(b);
                }
                catch(NullReferenceException e) {
                    Console.WriteLine(e.StackTrace);
                }
                catch (SocketException e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                string msg = "";

                for (int i = 0; i < k; i++)
                    msg += Convert.ToChar(b[i]);

                Console.WriteLine("Received in the Thread : " + msg);


              
                if (msg.Contains("ACTION_UP"))
                {
                    downFlag = false;
                    Console.WriteLine("Click Count : " + countForClickODrag);
                    clickSimulation(1);
                    countForClickODrag = 0;
                    continue;
                }
                else if (msg.Equals("ACTION_DOWN"))
                {
                    //clickSimulation(1);
                                  
                    downFlag = true;
                    continue;
                }
                else if (msg.Contains("ACTION_POINTER_3_DOWN"))
                {
                    clickSimulation(4);
                    continue;
                }
                else if (msg.Contains("ACTION_POINTER_2_DOWN"))
                {
                    
                    clickSimulation(3);
                    continue;
                }
                else if(msg.Contains("Scroll")){
                    int index=msg.IndexOf("Scroll");
                    try
                    {
                        string newmsg = msg.Substring(index, msg.Length);
                        string[] splitter = newmsg.Split(new string[] { " " }, StringSplitOptions.None);
                        foreach (string S in splitter)
                        {
                            Console.WriteLine(S);
                        }
                    
                    }
                    catch(ArgumentOutOfRangeException e){
                        Console.WriteLine(e.StackTrace);

                    }
                    scrollSimulation(1,1);
                    continue;
                }
                else if(downFlag) {
                    countForClickODrag++;
                }

                int flag = 0;
                string strx = "";
                string stry = "";

                foreach (char c in msg)
                {
                    if (c == 'x')
                    {
                        flag = 1;
                        continue;
                    }
                    if (c == 'y')
                    {
                        flag = 2;
                        continue;
                    }
                    if (flag == 1)
                    {
                        strx += c;
                    }
                    if (flag == 2)
                    {
                        stry += c;
                    }
                }
             
                try
                {
                    x = Convert.ToInt32(strx);
                    y = Convert.ToInt32(stry);
                }
                catch(FormatException e){
                    Console.WriteLine(e.StackTrace);
                }
                catch (OverflowException e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                if(x != 0 && y != 0) {
                    setCursor(x, y);
                }
            }
        }
        public static Cursor cursor;

        public void setCursor(int x, int y)
        {

            try
            {
                if (xresDesk > xresMob)
                    x *= (xresDesk / xresMob);
                else
                    x *= (xresMob / xresDesk);
                if (yresDesk > yresMob)
                    y *= (yresDesk / yresMob);
                else
                    y *= (yresMob / yresDesk);
            }
            catch(DivideByZeroException e) {
                Console.WriteLine(e.StackTrace);
            }

            x = (int)(x * 2);
            y = (int)(y * 2);

           // if (!downFlag)
           //{
           //     mouse_event(MOUSEEVENTF_LEFTUP, x, y, 0, 0);
           //     dragFlag = false;
           //}
            Cursor.Position = new Point(Cursor.Position.X - x, Cursor.Position.Y - y);

        }

        public void clickSimulation(int choice)
        {
            int X = Cursor.Position.X;
            int Y = Cursor.Position.Y;
            if (choice == 1 && countForClickODrag <= 3 && countForClickODrag > 0)
            {
                Console.WriteLine("Click Count :" + countForClickODrag);
                mouse_event(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, X, Y, 0, 0);
                countForClickODrag = 0;
            }
            if(choice == 3)
                mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, X, Y, 0, 0);
            if(choice == 4)
                mouse_event(MOUSEEVENTF_LEFTDOWN, X, Y, 0, 0);
            
        }
        public void scrollSimulation(int x,int y)
        {

            INPUT input = new INPUT();
            input.type = 0;
            input.mi.dx = 0;
            input.mi.dy = 0;
            input.mi.dwFlags = MOUSEEVENTF_WHEEL;
            input.mi.mouseData = 120;

            Console.WriteLine("Scrolling..");
            //mouse_event(MOUSEEVENTF_WHEEL, 0, 0, -120, 0);

            SendInput(1, ref input, Marshal.SizeOf(input));
        }
        public void MoveCursor()
        {

            xresDesk = SystemInformation.VirtualScreen.Width;
            yresDesk = SystemInformation.VirtualScreen.Height;


            IPAddress ip = IPAddress.Parse("192.168.43.5");
            TcpListener myList = new TcpListener(ip, 1212);
            try
            {
                myList.Start();
            }
            catch(SocketException e){
                Console.WriteLine(e.StackTrace);
            }

            try
            {
                s = myList.AcceptSocket();
            }
            catch (InvalidOperationException e)
            {
                Console.WriteLine(e.StackTrace);
            }


            byte[] b1 = new byte[100];
            string msg = "";
            int k = 0;

            try
            {
                k = s.Receive(b1);
                for (int i = 0; i < k; i++)
                    msg += Convert.ToChar(b1[i]);
            }
            catch (NullReferenceException e)
            {
                Console.WriteLine(e.StackTrace);
            }

            String temp = "";
            foreach (char c in msg)
            {

                if (c == 'n')
                {
                    continue;
                }

                temp += c;
                
            }
            try
            {
                xresMob = Convert.ToInt32(temp);
            }
            catch(FormatException e){
                Console.WriteLine(e.StackTrace);
            }

            try
            {
                k = s.Receive(b1);
            }
            catch(NullReferenceException e) {
                Console.WriteLine(e.StackTrace);
            }

            msg = "";

            for (int i = 0; i < k; i++)
                msg += Convert.ToChar(b1[i]);
            temp = "";
            foreach (char c in msg)
            {

                if (c == 'k')
                {
                    continue;
                }

                temp += c;
                
            }
            try
            {
                yresMob = Convert.ToInt32(temp);
            }
            catch (FormatException e)
            {
                Console.WriteLine(e.StackTrace);
            }
                    

            Thread mThread = new Thread(new ThreadStart(run));
            mThread.Start();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            MoveCursor();
        }

    }
}
