#!/usr/bin/ruby

def traverseDirectory(directory, array, basePath)
	files = Dir.entries(directory)
	files.each { |elem|
		typename = File.extname elem
		
        if elem == '.' or elem == '..' then
		
        elsif (typename == '.wacc')
            str = "#{directory}/#{elem}"
            str.slice!(basePath + "/")
			array.push(str)           
		
        elsif File.directory?("#{directory}/#{elem}")
			traverseDirectory("#{directory}/#{elem}", array, basePath)
		
        else
		
        end
	}
end

# Finds current directory
dir = Dir.pwd

basePath = "#{dir}"

# Enters wacc_examples directory
dir = File.expand_path("wacc_examples/valid", dir)

# Create array for filepaths of all .wacc files
arrayWaccFiles = []

traverseDirectory(dir, arrayWaccFiles, basePath)

# compiles the java files
system "make clean"
system "make"
	
# Tell the user the script is running
puts "\n########## RUNNING TEST ALL INVALID ##########"
puts

numberOfTests = arrayWaccFiles.length
numberOfSuccessfulTests = 0

# Excute grun on all files in the array
arrayWaccFiles.each { |x|
    command = "ruby compile " + x
    puts command
    system command
    puts

    exitCode = $?.exitstatus

    # remove generated file
    new_generated_file_path = File.basename(x, ".*") + ".s"
    command = "rm " + new_generated_file_path
    system command

    if exitCode == 0 then
    numberOfSuccessfulTests = numberOfSuccessfulTests + 1
    else
    end
}

puts
puts "YOUR SCORE IS:"
puts numberOfSuccessfulTests
puts "/" 
puts numberOfTests
