SpectrAnalysis was initially concieved of and written by Cameron Casey as a part of a research contract with professor Jim
Neitzel at the Evergreen State College. This program is intended for academic use only and not to be used for profit. This
project is open to the public for use and for further development. Other users are encouraged to add new features and
fine-tune existing ones to improve the utility of SpectrAnalysis.

SpecrtAnalysis makes use of three external libraries: jfreechart-1.0.17.jar and jcommon-1.0.21.jar by JFree, and
commons-math3-3.2.jar by Apache Commons. When cloning this repository, it may be necessary to reconfigure your buildpath
so that your computer knows where these libraries are. They can be found in the extralib folder within the parent directory.

When compiling and using the runnable jar for SpectrAnalysis, it is important that you create two folders in the same 
directory as the SpectrAnalysis.jar. These folders must be titled "standards" and "experimental". This is where you will 
put your spectra '.asc' files. If these folders are not present, the jar file will not execute. This repository has a set 
of working standards and experimental files already, but these can be switched out for other sets of spectra as desired.

The general guidelines for the operation of this program are:
1. Place a collection of standard spectra for ONE compound in the "standards" directory.
2. Place a collection of altered samples that you would like to compare to the standards in the "experimental" directory.
3. Run the jar file. Information on what each feature of the program does once running can be accessed by clicking the "Help" button.

At this time, it is not currently possible to refresh the files being read by SpectrAnalysis while it is running. If you would like to look at a new set of data files, you must replace the old files from the "standards" and "experimental" folders with your new files and open a new instance of SpectrAnalysis. This is because the contents of these folders is read at the time SpectrAnalysis is opened. This restriction may be changed in the future.
