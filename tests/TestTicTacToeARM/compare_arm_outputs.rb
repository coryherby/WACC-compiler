if ARGV.length != 2
  puts
  puts 'Error - Format should be : compare_arm_outputs.rb [file] [extension]'
  puts
  puts 'AVAILABLE commands : '
  puts
  puts 'compare_arm_outputs.rb [file] normal'
  puts 'compare_arm_outputs.rb [file] table'
  puts
else

  if ARGV[1] != 'normal' && ARGV[1] != 'table'

    puts
    puts 'AVAILABLE commands : '
    puts
    puts 'compare_arm_outputs.rb [file] normal'
    puts 'compare_arm_outputs.rb [file] table'
    puts

  else

    max_chars = 48
    starting_char_num = 55

    reference_file_name = 'TicTacToeARM'
    generated_file_name = ARGV[0]
    writing_file_name = 'TicTacToeARMOutput'
    writing_file_test_name_1 = 'TicTacToeARMOutputTest1'
    writing_file_test_name_2 = 'TicTacToeARMOutputTest2'

    reference_file = File.open(reference_file_name, 'r')
    generated_file = File.open(generated_file_name, 'r')
    writing_file = File.new(writing_file_name, 'w+')
    writing_file_test_1 = File.new(writing_file_test_name_1, 'w+')
    writing_file_test_2 = File.new(writing_file_test_name_2, 'w+')

    reference_file_lines = Array.new
    generated_file_lines = Array.new

    number_of_lines = 0

    reference_file.each_line do |line|

      line_transformed = line.sub(/[0-9]+(\t|)/, '')
      line_transformed = line_transformed.sub(/\t/, "    ")

      if line_transformed.length > max_chars
        line_transformed = line_transformed.slice(0, max_chars)
      end

      reference_file_lines.push(line_transformed.to_s)

      number_of_lines = number_of_lines + 1
    end

    generated_file.each_line do |line|

      line_transformed = line.to_s
      line_transformed = line_transformed.sub(/[0-9]+(\t|)/, '')
      line_transformed = line_transformed.sub(/\t/, "    ")

      if line_transformed.length > max_chars
        line_transformed = line_transformed.slice(0, max_chars)
      end

      generated_file_lines.push(line_transformed)
    end

    (0..number_of_lines-1).each { |i|

      reference_line = reference_file_lines[i].to_s
      generated_line = generated_file_lines[i].to_s

      reference_line.to_s.delete("\n")

      writing_file_test_1.write(reference_line)
      writing_file_test_2.write(generated_line)

      while reference_line.length < starting_char_num
        reference_line = reference_line << ' '
      end

      reference_line = reference_line.to_s.delete("\n") + generated_line.to_s

      writing_file.write(reference_line)
    }

    # print the diff on the terminal and remove files

    extension1 = ' -s -B --report-identical-files --normal '
    extension2 = ' -y -s -B --report-identical-files --side-by-side --suppress-common-lines --ignore-blank-lines '

    if ARGV[1] == 'normal'
      extension = extension1
    else
      extension = extension2
    end

    command = 'diff ' + extension + writing_file_test_name_1 + ' ' + writing_file_test_name_2
    system command
    command = 'rm ' + writing_file_test_name_1 + ' ' + writing_file_test_name_2
    system command

  end

end

