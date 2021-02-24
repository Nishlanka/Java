package com.cicl.system.CICLIT_SYSTEM;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC_MY {
	 static String  url="jdbc:mysql://127.0.0.1:3306/person?zeroDateTimeBehavior=convertToNull";


	    public static Connection con() throws Exception {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection c=DriverManager.getConnection(url,"root", "abcc");
	        return c;

}
}

package com.cicl.system.inqfrm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.cicl.CICLIT_SYSTEM.SysPath;
import com.cicl.system.CICLIT_SYSTEM.JDBC_MY;

import com.mysql.cj.api.x.Result;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Link;

public class req_formSub extends req_form {
	
	public Window uploader;
	String user;
	String branch;
	String epf_no;
	int risultato=-1;
	int rowaffected=0;
	String brnchshort;
	String formatted;
	int a;
	Upload upload;
	public req_formSub() {
		
		textField_CNO.setMaxLength(10);
		
		table_PRELIST.addContainerProperty("TOKEN ID",String.class,null);
		table_PRELIST.addContainerProperty("COMPLAINED DATE",String.class,null);
		table_PRELIST.addContainerProperty("STATUS",String.class,null);
		table_PRELIST.addContainerProperty("Complete Message",String.class,null);
		table_PRELIST.addContainerProperty("Hold Message",String.class,null);
		table_PRELIST.addContainerProperty("Completed By",String.class,null);
		table_PRELIST.addContainerProperty("Hold By",String.class,null);
		table_PRELIST.addContainerProperty("Upload",Button.class,null);
		
		table_PRELIST.setColumnWidth("TOKEN ID", 90);
		table_PRELIST.setColumnWidth("COMPLAINED DATE", 95);
		comboBox_REASONS.setNullSelectionAllowed(false);
		comboBox_PRIO.setNullSelectionAllowed(false);
		textField_CNO.addValidator(new RegexpValidator("^[0-9]{10}$","Invalied Contact NO"));
		//loadtable();
		
		textField_CNO.addBlurListener(new BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
			
				if(textField_CNO.isValid()==false)
				{
					textField_CNO.setValidationVisible(true);
				}
				
				else if(textField_CNO.isValid()==true)
				{
					textField_CNO.setValidationVisible(false);
				}
			}
		});		
		
		loadCombo();
		
		btn_SUBMIT.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			
				System.out.println(comboBox_REASONS.getValue());
				if(textField_CNO.isValid()==false)
				{	
					Notification.show("Invalied Details");
					textField_CNO.focus();
				}
				
				else if(textField_NAME.getValue().toString().trim().equals("")) {
					   Notification.show("Invalied Details");
				}
				
				else if(textField_BRANCH.getValue().toString().trim().equals("")) {
					 Notification.show("Invalied Details");
				}
				
				else if(textField_CNO.getValue().toString().trim().equals("")) {
					   Notification.show("Enter the phone No");
				}
				
				else if(comboBox_REASONS.getValue()==null) {
					Notification.show("Enter a Reason");
				}
				
				else if(comboBox_PRIO.getValue()==null) {
					Notification.show("Enter a Priority");
				}
				
				
				else {
					
					Connection co = null;
					Statement st = null;
					ResultSet ds=null;
					ResultSet rse=null;
					
					try {
							
						co = JDBC_MY.con();
						st = co.createStatement();
						
						
						String date="";
						SimpleDateFormat frmtter=new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
						date=frmtter.format(new java.util.Date(System.currentTimeMillis()));
						
						String date_noTme="";
						SimpleDateFormat frmttr=new SimpleDateFormat("dd/MM/yyyy");
						date_noTme=frmttr.format(new java.util.Date(System.currentTimeMillis()));
						
						System.out.println(date_noTme+"uyiuyiuyi");
						String des=textArea_DES.getValue().toString();
						System.out.println(des);
					
						int frepf = Integer.parseInt(epf_no.trim());
						String epf_no = new DecimalFormat("0000").format(frepf);
						
						String sql=("insert into coop_sys.req_form_details(user_name,user_branch,user_tpno,user_reasons,data_enterDate,epf,priority_level,user_description,dataEnterDate_notime) values('"+textField_NAME.getValue()+"','"+textField_BRANCH.getValue()+"','"+textField_CNO.getValue().trim().toString()+"','"+comboBox_REASONS.getValue().toString()+"','"+date+"','80"+epf_no+"','"+comboBox_PRIO.getValue().toString()+"','"+textArea_DES.getValue().trim().toString().replaceAll("'", "’")+"','"+date_noTme+"')");
						
						
						
						try {
							rowaffected=st.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
							ResultSet rss=st.getGeneratedKeys();
							if(rss.next()) {
								risultato=rss.getInt(1);
								System.out.println(risultato+"getlastid");
							}
						}catch(Exception e) {
							e.printStackTrace();
						}
						if(rowaffected>0) {
							Notification.show("Data Entered Succesfully");
							
							//loadtable();
						}
						else {
							Notification.show("Value Not Added");
						}
						
						try {
							ds=st.executeQuery("select * from coop_sys.req_form_details where id='"+risultato+"'");
							while(ds.next()) {
								a=ds.getInt("id");
								formatted=String.format("%08d", a);
								brnchshort=ds.getString("user_branch").substring(0, 4).toUpperCase();
								
								System.out.println(formatted+"number");
								System.out.println(risultato+"lastid");
							}
							
							String br="update coop_sys.req_form_details set tokenId='"+brnchshort+""+formatted+"' where id='"+risultato+"'";
							st.execute(br);
					
							
							Window sal = new Window("Confirm");
							Inq_tid_formSUB Inq_tid_formSUB=new Inq_tid_formSUB();
							String suser="";
							suser="<caption align='bottom'><b><font size='2'><font color='black'>" + "<br>" + "USER" + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp: "  + user + "<br>" + "BRANCH  : " + branch + "<br>" + "TOKEN ID  : "  +brnchshort+""+formatted+ "<br>" + "Please Confirm Details "+ "</caption></b></font>";
							Inq_tid_formSUB.label_ok.setValue(suser);
							Inq_tid_formSUB.button_OK.addClickListener(new ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
									// TODO Auto-generated method stub
									
									sal.close();
								}
							});
							
							sal.setContent(Inq_tid_formSUB);
							
							sal.setWidth("300px");
							sal.setHeight("200px");
							sal.center();
							sal.setModal(true);
							sal.setResizable(false);
							UI.getCurrent().addWindow(sal);
						
							
							
						}catch(Exception e) {
							e.printStackTrace();
							Notification.show("Error",e.getMessage(),Notification.TYPE_ERROR_MESSAGE);
						}
						try {
							rse=st.executeQuery("select * from coop_sys.req_form_details where id='"+risultato+"'");
							while(rse.next()) {
								label_REF.setValue(rse.getString("tokenId"));
								loadtable();
							}	
						
						textField_CNO.setValue("");
						comboBox_REASONS.setValue(null);
						textArea_DES.setValue("");
						comboBox_PRIO.setValue(null);
				
						
						}catch(Exception e) {
							
						}
					}catch(Exception e) {
						e.printStackTrace();
					}finally {
						try {
							if (st != null) {
								st.close();
							}
							if (co != null) {
								co.close();
							}
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				
				
			
					
				}
				
			}
		});
		

		
	}
	
	void loadtable() {
		Connection co = null;
		Statement st = null;
		ResultSet rs=null;
		table_PRELIST.removeAllItems();
		try {
		co = JDBC_MY.con();
		st = co.createStatement();
		
		int frepf=Integer.parseInt(epf_no.trim());
		String epf_no= new DecimalFormat("0000").format(frepf);
		
		String sql="select * from coop_sys.req_form_details where epf='80"+epf_no+"'order by id DESC";
		
		rs=st.executeQuery(sql); 
		
		while(rs.next()) {
			
			
			
			String tID=rs.getString("tokenId");
			String enterDate=rs.getString("data_enterDate");
			String dateNoTime=rs.getString("dataEnterDate_notime");
			String adminUsr=rs.getString("admin_panel_user");
			int status=rs.getInt("status");
			String comMsg=rs.getString("comp_reasons");
			String holdMsg=rs.getString("hold_reasons");
			
			String completeBy="";
			String holdBy="";
			System.out.println(table_PRELIST.size());
			
			Button bt_click1=new Button("UPLOAD",FontAwesome.UPLOAD);
			
			
			
			//bt_click1.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		
			//fonticon://FontAwesome/f0c7
		//	bt_click1.setI
			bt_click1.setId(Integer.toString(table_PRELIST.size()));
			
			
			String stat="";
		
			if(status==1 && adminUsr==null) {
			
				stat="PENDING";
			}
			
			if(status==1 && adminUsr!=null) {
				
				stat="HOLD UNCOMPLETE";
				holdBy=rs.getString("admin_panel_user");
			}
			
			if(status==2) {
				
				stat="COMPLETED";
				completeBy=rs.getString("admin_panel_user");
			}
			
			if(status==3) {
				
				stat="HOLD";
				holdBy=rs.getString("admin_panel_user");
			}
			
			if(stat=="COMPLETED") {
				
				bt_click1.setVisible(false);
			}
			
			
			bt_click1.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
			String ss=(String) table_PRELIST.getContainerProperty(Integer.parseInt(bt_click1.getId()), "TOKEN ID").getValue();
					
							//setProfilePIC("change");
					
					
					class ImageUploader implements Receiver, SucceededListener {
						/**
						 ** 
						 **/
						private static final long serialVersionUID = 1L;
						public File file;

						@Override
						public OutputStream receiveUpload(String filename, String mimeType) {
							// Create upload stream
							FileOutputStream fos = null; // Stream to write to
							try {
								Notification.show("uploading...");
								System.out.println(ss);
								// Open the file for writing.
								file = new File(SysPath.GetSysPath() + "tmp_img/" + filename);
								fos = new FileOutputStream(file);
							} catch (final java.io.FileNotFoundException e) {
								new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
										.show(Page.getCurrent());
								// upload.setButtonCaption("Start Upload");
								return null;
							}
							return fos; // Return the output stream to write to
						}

						@Override
						public void uploadSucceeded(SucceededEvent event) {
							// Show the uploaded file in the image viewer
						
							try {
								uploader.close();
								Notification.show("Image Successfully Uploaded ! ");
								
								//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								
								java.io.File fplder = new java.io.File("\\\\172.20.10.14\\CICL_Complaint_mgnt\\" + branch +"\\" + 80 + epf_no +"\\" + ss);
								
								if(!fplder.exists()) {
									fplder.mkdirs();
								}
								
								System.out.println(risultato+"checkkkkkkkkkkkkkk");
								//java.io.File photo = new java.io.File("C:\\APPS\\rdb_img\\" + user +"_" + System.currentTimeMillis() + ".png");
								java.io.File photo = new java.io.File(fplder,+ 80 + epf_no +"_" + System.currentTimeMillis() + ".png");
								net.coobird.thumbnailator.Thumbnails.of(new java.io.File(file.getAbsolutePath()))
										.size(1024, 768).toFile(photo);

								if (photo.exists()) {
								
									FileResource fr = new FileResource(photo);

									fr.setCacheTime(0);
									
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
										.show(Page.getCurrent());
								// upload.setButtonCaption("Start Upload");
								e.printStackTrace();
							}

						}
					}
					
					ImageUploader receiver = new ImageUploader();
					upload = new Upload("Upload Image Here", receiver);
					upload.setButtonCaption("Start Upload");
					upload.addSucceededListener(receiver);
					uploader = new Window("Uploader");
					VerticalLayout vt = new VerticalLayout();
					vt.addComponent(upload);
					uploader.setContent(vt);
					uploader.setWidth("400px");
					uploader.setHeight("150px");
					uploader.setModal(true);
					uploader.setResizable(false);
					UI.getCurrent().addWindow(uploader);
						
				}
			});
			
			table_PRELIST.addItem(new Object[] {tID,dateNoTime,stat,comMsg,holdMsg,completeBy,holdBy,bt_click1},table_PRELIST.size());
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (st != null) {
					st.close();
				}
				if (co != null) {
					co.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
void loadCombo() {
		
		Connection co=null;
		Statement stmt=null;
		ResultSet rss=null;
		try {
			
			co = JDBC_MY.con();
			stmt = co.createStatement();
			
			//Vector<String> v=new Vector<>();
			Vector<String> v1=new Vector<>();
			
			String sql="select reasons from coop_sys.it_dept_reasons";
			rss=stmt.executeQuery(sql);
			
			while(rss.next()) {
				
				//v.add(rss.getString("tokenId"));
				v1.add(rss.getString("reasons"));
			}
			
			//comboBox_TID.addItems(v);
			comboBox_REASONS.addItems(v1);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (co != null) {
					co.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		
		
	}
	
public void getUser(String user) {
		
		this.user=user;
		System.out.println(user);
	}

public void getUser12(String epf,String brh, int level,String usr,String brc) {
	// TODO Auto-generated method stub
	
	textField_EPF.setValue(epf);

	
	
	user=usr;
	branch = brh;
	epf_no=epf;
	////////////////////////////////////////////////////
	Connection co = null;
	Statement st = null;
	loadtable();
	try {
		textField_EPF.setValue(epf);
		System.out.println(branch);
		int frepf = Integer.parseInt(epf_no.trim());
		String epf_no = new DecimalFormat("0000").format(frepf);
		// boolean epf_not=false;
		co = JDBC_MY.con();
		st = co.createStatement();
		ResultSet rsq = st
				.executeQuery("SELECT * FROM e_details.e_details where  emp_id='80" + epf_no + "'");
		
		
		if (rsq.next()) {
			// System.out.println(rsq.getString("FirstName"));

			
			String empid=rsq.getString("emp_id");
			String name=rsq.getString("FirstName");
			String nic=rsq.getString("NIC");
			String des=rsq.getString("Designation");
			String location1 = rsq.getString("Location");
			//String loc=rsq.getString("Location");

				

				
					
					textField_NAME.setEnabled(false);
					textField_BRANCH.setEnabled(false);
					textField_DES.setEnabled(false);
					textField_EPF.setEnabled(false);
					textField_BRANCH.setValue(location1);
					textField_DES.setValue(des);
					textField_NAME.setValue(name);
					//textField_EPF.setValue(epf_no);
					
			
						
			 
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (st != null) {
				st.close();
			}
			if (co != null) {
				co.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			// TODO: handle exception
		}
	}
	}

	
	
	
	
}
