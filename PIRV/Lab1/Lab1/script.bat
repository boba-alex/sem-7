for %%a in (500 1000) do (
	echo %%a>>output.txt
	for %%c in (1 100 500) do (
		for %%b in (0 1 2) do (
			start /wait lab1 %%a %%a %%a %%c %%b
		)
		echo %%c>>output.txt
	)
)


