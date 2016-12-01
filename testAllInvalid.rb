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
dir = File.expand_path("wacc_examples/invalid/syntaxErr", dir)

# Create array for filepaths of all .wacc files
arrayWaccFiles = []

traverseDirectory(dir, arrayWaccFiles, basePath)

# compiles the java files
system "make clean"
system "make"

numberOfTestsSyntax = arrayWaccFiles.length
numberOfSuccessfulTestsSyntax = 0

# Tell the user the script is running
puts "\n########## RUNNING TEST ALL INVALID ##########"
puts

# Excute grun on all files in the array
arrayWaccFiles.each { |x|
    command = "ruby compile " + x
    puts command
    system command
    puts

    exitCode = $?.exitstatus

    if exitCode == 100 then
    numberOfSuccessfulTestsSyntax = numberOfSuccessfulTestsSyntax + 1
    else
    end
}

# Finds current directory
dir = Dir.pwd

basePath = "#{dir}"

# Enters wacc_examples directory
dir = File.expand_path("wacc_examples/invalid/semanticErr", dir)

# Create array for filepaths of all .wacc files
arrayWaccFiles = []

traverseDirectory(dir, arrayWaccFiles, basePath)

numberOfTestsSemantic = arrayWaccFiles.length
numberOfSuccessfulTestsSemantic = 0

# Excute grun on all files in the array
arrayWaccFiles.each { |x|
    command = "ruby compile " + x
    puts command
    system command
    puts

    exitCode = $?.exitstatus

    if exitCode == 200 then
    numberOfSuccessfulTestsSemantic = numberOfSuccessfulTestsSemantic + 1
    else
    end
}

puts
puts "YOUR SCORE FOR SYNTAX IS:"
puts numberOfSuccessfulTestsSyntax
puts "/"
puts numberOfTestsSyntax

puts
puts "YOUR SCORE FOR SEMANTIC IS:"
puts numberOfSuccessfulTestsSemantic
puts "/"
puts numberOfTestsSemantic

puts
puts "YOUR TOTAL SCORE IS:"
puts (numberOfSuccessfulTestsSemantic + numberOfSuccessfulTestsSyntax)
puts "/"
puts (numberOfTestsSemantic + numberOfTestsSyntax)
